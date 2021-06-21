package com.aruoxi.ebookshop.controller.restController;

import cn.leancloud.AVFile;
import com.aruoxi.ebookshop.common.CommonResult;
import com.aruoxi.ebookshop.common.RegexUtil;
import com.aruoxi.ebookshop.controller.dto.BookSearchDto;
import com.aruoxi.ebookshop.controller.dto.BookUploadDto;
import com.aruoxi.ebookshop.controller.restController.dto.BookContent;
import com.aruoxi.ebookshop.controller.restController.dto.BookPage;
import com.aruoxi.ebookshop.controller.restController.dto.FileDto;
import com.aruoxi.ebookshop.domain.Book;
import com.aruoxi.ebookshop.exception.ResourceNotFoundException;
import com.aruoxi.ebookshop.repository.BookRepository;
import com.aruoxi.ebookshop.service.impl.BookServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import org.apache.catalina.Store;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Tag(name = "书籍API接口")
@RestController
@RequestMapping("/api/books")
public class RestBookController {

    @Value("${ebookshop.file.filePrefix}")
    private String filePrefix;
    private static final Logger log = LoggerFactory.getLogger(RestBookController.class);
    private static final Logger LOG = LoggerFactory.getLogger(RestBookController.class);
    @Resource
    private BookServiceImpl bookService;
    @Resource
    private BookRepository bookRepository;
    @Resource
    private RegexUtil regexUtil;

    public static CommonResult getBookUrl(@RequestParam("bookId") Long bookId, BookRepository bookRepository) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            return CommonResult.success(book.getBookUrl());
        }
        return CommonResult.fail(HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    // 分页查询指定条件的所有书籍
    @GetMapping
    @Operation(summary = "分页书籍列表", description = "根据BookSearchDto查询书籍")
    @HystrixCommand(fallbackMethod = "findAllHystrix")
    public CommonResult<Page<Book>> findAll(@RequestBody BookSearchDto search) {
        log.info("search = " + search);
        Integer pageNum = search.getPageNum();
        Integer pageSize = search.getPageSize();
        String bookName = search.getBookName();
        Boolean canRead = search.getCanRead();

        return CommonResult.success(bookService.findPage(new BookSearchDto(bookName, pageNum, pageSize, canRead)));
    }

    //备选方案
    public CommonResult<Page<Book>> findAllHystrix(@RequestBody BookSearchDto search){

        Page<Book> bookPage=null;
        return CommonResult.success(bookPage);
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "根据id查询书籍", description = "根据id查询书籍")
    @HystrixCommand(fallbackMethod = "findByIdHytrix")
    public CommonResult<Book> findById(@PathVariable  @Parameter(description = "书籍id") Long id) {
        Book book=bookService.findById(id);
        if(book==null)throw new RuntimeException("无此书本");

        return CommonResult.success(book);
    }

    //错误备选方案
    public CommonResult<Book> findByIdHytrix(@PathVariable  @Parameter(description = "书籍id") Long id){
        Book book=new Book();
        book.setBookName("找不到对应信息");
        book.setBookCoverImg("找不到对应信息");
        book.setBookUri("找不到对应信息");
        book.setAuthor("找不到对应信息");
        book.setBookSellStatus("找不到对应信息");
        book.setTag("找不到对应信息");
        return CommonResult.success(book);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
    @RequestMapping(value = "/{bookID}/pages", method = RequestMethod.GET)
    @Operation(summary = "根据id获取书籍所有内容", description = "根据id获取书籍所有内容")
    @HystrixCommand(fallbackMethod = "findBookContentsHytrix")
    public CommonResult<BookContent> bookAllContents(@PathVariable @Parameter(description = "书籍id") Long bookID) throws IOException, ResourceNotFoundException {
        try {
            Book book = bookService.findById(bookID);
            int totalPageNum = bookService.getTotalPageNum(bookID);

            return CommonResult.success(new BookContent(bookID, book.getBookName(), book.getAuthor(), totalPageNum, false, bookService.getAllBookContent(bookID)));
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    //错误备选方案
    public CommonResult<BookContent> findBookContentsHytrix(@PathVariable @Parameter(description = "书籍id") Long bookID){

        Book book = new Book();
        book.setBookName("找不到对应信息");
        book.setBookCoverImg("找不到对应信息");
        book.setBookUri("找不到对应信息");
        book.setAuthor("找不到对应信息");
        book.setBookSellStatus("找不到对应信息");
        book.setTag("找不到对应信息");
        int totalPageNum = 1;

        return CommonResult.success(new BookContent(bookID, book.getBookName(), book.getAuthor(), totalPageNum, false, null));

    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
    @RequestMapping(value = "/{bookID}/pages/{bookPagesSearch}", method = RequestMethod.GET)
    @Operation(summary = "根据id及页码pageNum获取书籍具体页面内容", description = "根据id及页码pageNum获取书籍具体页面内容")
    @HystrixCommand(fallbackMethod = "bookContentsHystrix")
    public CommonResult<BookContent> bookContents(@PathVariable @Parameter(description = "书籍id") Long bookID, @PathVariable @Parameter(description = "书籍页码", example = "1, 2-3") String bookPagesSearch) throws IOException, ResourceNotFoundException {
        Book book = bookService.findById(bookID);
        int totalPageNum = bookService.getTotalPageNum(bookID);
        boolean isSinglePage = true;
        ArrayList<BookPage> pages = new ArrayList<BookPage>();

        // 如果获取的单个页面，例如 api/books/30/pages/1 ,即第1页
        if (regexUtil.isNumber(bookPagesSearch)) {
            int pageNum = Integer.parseInt(bookPagesSearch);
            if (pageNum > totalPageNum) {
                return CommonResult.success(new BookContent(bookID, book.getBookName(), book.getAuthor(), totalPageNum, true, null));
            }

            String content = bookService.getbookContent(bookID, pageNum);
            pages.add(new BookPage(pageNum, content));

            // 如果获取的是页面范围，例如 api/books/30/pages/1-10 ,即第1页到第10页
        } else if (regexUtil.isPageStartAndEnd(bookPagesSearch)) {
            isSinglePage = false;
            String[] strings = bookPagesSearch.split("-");
            int pageStart = Integer.parseInt(strings[0]);
            int pageEnd = Integer.parseInt(strings[1]);

            if (pageStart > totalPageNum) {
                return CommonResult.success(new BookContent(bookID, book.getBookName(), book.getAuthor(), totalPageNum, false, null));
            } else if (pageStart == totalPageNum) {
                pages.add(new BookPage(pageStart, bookService.getbookContent(bookID, pageStart)));
            } else if (pageEnd > totalPageNum) {
                pageEnd = totalPageNum;
            }

            for (int i = pageStart; i <= pageEnd; i++) {
                pages.add(new BookPage(i, bookService.getbookContent(bookID, i)));
            }
        } else {
            throw new ResourceNotFoundException();
        }

        return CommonResult.success(new BookContent(bookID, book.getBookName(), book.getAuthor(), totalPageNum, isSinglePage, pages));
    }

    //备选方案
    public CommonResult<BookContent> bookContentsHystrix(@PathVariable @Parameter(description = "书籍id") Long bookID, @PathVariable @Parameter(description = "书籍页码", example = "1, 2-3") String bookPagesSearch) throws IOException, ResourceNotFoundException {
        Book book = new Book();
        book.setBookName("找不到对应信息");
        book.setBookCoverImg("找不到对应信息");
        book.setBookUri("找不到对应信息");
        book.setAuthor("找不到对应信息");
        book.setBookSellStatus("找不到对应信息");
        book.setTag("找不到对应信息");
        int totalPageNum=1;
        boolean isSinglePage=false;
        ArrayList<BookPage> pages = null;
        return CommonResult.success(new BookContent(bookID, book.getBookName(), book.getAuthor(), totalPageNum, isSinglePage, pages));
    }

//    @RequestMapping(value = "/{bookID}/pages/{pageNum}", method = RequestMethod.GET)
//    @Operation(summary = "根据id及页码pageNum获取书籍具体页面内容", description = "根据id及页码pageNum获取书籍具体页面内容")
//    public CommonResult<BookContent> bookContent(@PathVariable  @Parameter(description = "书籍id") Long bookID, @PathVariable  @Parameter(description = "书籍页码") Integer pageNum) throws IOException {
//        Book book = bookService.findById(bookID);
//        String author = book.getAuthor();
//        Integer totalPageNum = bookService.getTotalPageNum(bookID);
//        String content = bookService.getbookContent(bookID, pageNum);
//        BookContent bookContent = new BookContent(bookID, author, totalPageNum, pageNum, content);
//
//        return CommonResult.success(bookContent);
//    }

    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "保存书籍的实体",
            description = "上传新增书籍",
            security = @SecurityRequirement(name = "需要admin权限"))
    public CommonResult<Object> add(@RequestBody Book book) {
        bookService.save(book);
        return CommonResult.success(book);
    }

    // 上传文件会自动绑定到MultipartFile中
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PostMapping(value = "/upload")
    @ResponseBody
    @Operation(summary = "上传书籍",
            description = "上传书籍",
            security = @SecurityRequirement(name = "需要admin权限"))
    public CommonResult upload(FileDto fileDto
//            MultipartFile uploadFile,
//        , @RequestParam(value = "bookName", required = false) String bookName, @RequestParam(value = "bookAuthor", required = false) String bookAuthor, @RequestBody JSONPObject jsonpObject
//                               BookUploadDto uploadBookInfo
    ) throws Exception {
//        log.info("jsonpObject = " + jsonpObject);

        // 如果文件不为空，写入上传路径
        if (!fileDto.getUploadFile().isEmpty()) {
            // 获取资源目录

//            String uploadFilePath = this.getServletContext().getRealPath("/WEB-INF/upload");
//            log.info("uploadFilePath = " + uploadFilePath);
            String bookName = fileDto.getUploadBookInfo().getBookName();
            String bookAuthor = fileDto.getUploadBookInfo().getAuthor();
            Float price = fileDto.getUploadBookInfo().getPrice();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
            //构建文件上传所要保存的"文件夹路径"--这里是相对路径，保存到项目根路径的文件夹下
            String realPath = "src/main/resources/" + filePrefix;
            LOG.info("-----------上传文件保存的路径【" + realPath + "】-----------");
            String format = sdf.format(new Date());
            //存放上传文件的文件夹
            File file = new File(realPath + format);
            LOG.info("-----------存放上传文件的文件夹【" + file + "】-----------");
            LOG.info("-----------输出文件夹绝对路径 -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径【" + file.getCanonicalPath() + "】-----------");
            if (!file.isDirectory()) {
                //递归生成文件夹
                file.mkdirs();
            }

            // 获取原始的名字  original:最初的，起始的  方法是得到原来的文件名在客户机的文件系统名称
            String oldName = fileDto.getUploadFile().getOriginalFilename();
            // 第一个.前的名字
            String OriginalFilename = oldName.substring(0, oldName.indexOf('.'));
            LOG.info("-----------文件原始的名字【" + oldName + "】-----------");
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."),oldName.length());
            LOG.info("-----------文件要保存后的新名字【" + newName + "】-----------");
            try {
                String theBookName = bookName != null ? bookName : OriginalFilename;
                String theBookAuthor = bookAuthor != null ? bookAuthor : "EBookShop";
                float theprice = price != null ? price : 0f;
                LOG.info("theBookName = " + bookName);
                LOG.info("theBookAuthor = " + bookAuthor);
                LOG.info("theprice = " + price);

                // 上传云端
//                Map<String, Object> meta = new HashMap<String, Object>();
//                meta.put("mime_type", "text/plain");
                AVFile cloudFile = new AVFile(oldName, fileDto.getUploadFile().getBytes());
                cloudFile.setMimeType("text/plain");
                cloudFile.saveInBackground(true).subscribe(new Observer<AVFile>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {}

                    @Override
                    public void onNext(AVFile file) {
                        LOG.debug("文件保存完成 objectId：" + file.getObjectId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LOG.debug("failed to get data. cause: " + throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {}

                });;
                String bookUrl = cloudFile.getUrl();
                LOG.info("cloudFile.getUrl()" + bookUrl);

                //构建真实的文件路径
                File newFile = new File(file.getAbsolutePath() + File.separator + newName);
                //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
                fileDto.getUploadFile().transferTo(newFile);
//                String filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploadFile/" + format + newName;
//                String filePath = (file.getCanonicalPath() + "/" + newName).replace('\\','/');
                String filePath = (file.getPath() + File.separator + newName);
                if (File.separator.equals("/")) {
                    filePath = filePath.replace('\\', '/');
                }
                filePath.replace("//", "\\\\");
                LOG.info("-----------【" + filePath + "】-----------");

                Book book = new Book();
                book.setBookName(theBookName);
                book.setAuthor(theBookAuthor);
                book.setOriginalPrice(theprice);
                book.setSellingPrice(theprice - 0.2f >= 0 ? theprice - 0.2f : 0);
                book.setBookUri(filePath);
                book.setBookUrl(bookUrl);

                bookService.save(book);

                return  CommonResult.success(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //            // 上传文件路径
//            String path = request.getServletContext().getRealPath("");
//            LOG.info("path = " + path);
//            // 上传文件名
//            String filename = uploadFile.getOriginalFilename();
//            // 将上传文件保存到一个目标文件当中
//            String newPath = filePrefix + filename;
//            LOG.info("filePrefix" + filePrefix);
//            //将临时文件转存到我们的指定目录下
//            uploadFile.transferTo(new File(newPath));
//
//            //获取不带后缀的文件名，并存在数据库里
//            String newFilename;
//            int dot = filename.lastIndexOf('.');
//            if ((dot > -1) && (dot < (filename.length()))) {
//                newFilename = filename.substring(0, dot);
//            } else {
//                newFilename = filename;
//            }
//            Book book = new Book();
//            book.setBookName(newFilename);
//            book.setBookUri(newPath);
//            bookService.save(book);
//
//            return CommonResult.success("上传成功");

        }
        return CommonResult.fail(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PutMapping
    @Operation(summary = "修改书籍",
            description = "修改书籍",
            security = @SecurityRequirement(name = "需要admin权限"))
    public CommonResult<Object> update(@RequestBody Book book) {
        bookService.save(book);
        return CommonResult.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "删除书籍",
            description = "根据id删除书籍",
            security = @SecurityRequirement(name = "需要admin权限"))
    public void delete(@PathVariable("id") Long id) {
        bookService.delete(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
    @ResponseBody
    @RequestMapping(value = "/downloadUrl", method = RequestMethod.GET)
    @Hidden
    public CommonResult download(HttpServletRequest request,
                                 @RequestHeader("User-Agent") String userAgent,
                                 @RequestParam("bookId") Long bookId) throws Exception {
        return getBookUrl(bookId, bookRepository);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> download1(@RequestBody FileDto fileDto
//            HttpServletRequest request,
//                                            @RequestHeader("User-Agent") String userAgent,
//                                            @RequestParam("bookId") Integer bookId
    ) throws Exception {
        LOG.info("userAgent = " + fileDto.getUserAgent());
        Book book = bookRepository.findById(fileDto.getBookId().longValue()).orElse(null);
        if (book == null) {
            byte[] a = new byte[100];
            return new ResponseEntity<byte[]>(a,HttpStatus.NOT_FOUND);
        }

        // 下载文件路径
        String path = book.getBookUri();

        int lastIndex = path.lastIndexOf(".");
//        int lastIndex1 = path.lastIndexOf(File.separator);
//        log.info("filename1" + path.substring(lastIndex1));

        // 构建File
        String filename = book.getBookName() + path.substring(lastIndex);
        LOG.info("path= " + path);
        LOG.info("File.separator= " + File.separator);
        LOG.info(filename);
//      File file = new File(path + File.separator + filename);
        File file = new File(path);
        log.info("fielPath= " + file.getCanonicalPath());
        // ok表示Http协议中的状态 200
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        // 内容长度
        builder.contentLength(file.length());
        // application/octet-stream ： 二进制流数据（最常见的文件下载）。
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        // 使用URLDecoder.decode对文件名进行解码
        filename = URLEncoder.encode(filename, "UTF-8");
        // 设置实际的响应文件名，告诉浏览器文件要用于【下载】、【保存】attachment 以附件形式
        // 不同的浏览器，处理方式不同，要根据浏览器版本进行区别判断
        if (fileDto.getUserAgent().indexOf("MSIE") > 0) {
            // 如果是IE，只需要用UTF-8字符集进行URL编码即可
            builder.header("Content-Disposition", "attachment; filename=" + filename);
        } else {
            // 而FireFox、Chrome等浏览器，则需要说明编码的字符集
            // 注意filename后面有个*号，在UTF-8后面有两个单引号！
            builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
        }
        return builder.body(FileUtils.readFileToByteArray(file));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN','SELLER')")
    @Operation(summary = "书籍内容",
            description = "根据搜索条件返回相应的书籍分页内容",
            security = @SecurityRequirement(name = "需要登录"))
    @GetMapping(value = "/content")
    @Hidden
    public CommonResult<String> findContent(@PathVariable(value = "pageNum")int pageNum,@PathVariable(value = "BookId") Long BookId) throws IOException {
        return CommonResult.success(bookService.getbookContent(BookId,pageNum));
    }

}


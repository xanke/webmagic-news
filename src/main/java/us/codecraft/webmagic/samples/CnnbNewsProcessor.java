package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;

import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 */
public class CnnbNewsProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private static final String URL_LIST = "http://nbnews\\.cnnb\\.com\\.cn/(\\w+)";
    private static final String URL_POST = "http://news\\.cnnb\\.com\\.cn/(\\w+).*";

    @Override
    public void process(Page page) {

        if (page.getUrl().regex(URL_LIST).match()) {
            List<String> urls = page.getHtml().css(".fiveBox").links().all();
            page.addTargetRequests(urls);
            //文章页
            System.out.println(urls);
        } else {
            page.putField("title", page.getHtml().$(".heading", "text").toString());
            page.putField("date", page.getHtml().$("span.time", "text").toString());
            page.putField("content", page.getHtml().$(".article"));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new CnnbNewsProcessor()).addPipeline(new JsonFilePipeline("C:\\webmagic\\"))
                .addUrl("http://nbnews.cnnb.com.cn/sz/").thread(5).run();

        Spider.create(new CnnbNewsProcessor()).addPipeline(new JsonFilePipeline("C:\\webmagic\\"))
                .addUrl("http://nbnews.cnnb.com.cn/cj/").thread(5).run();

    }
}

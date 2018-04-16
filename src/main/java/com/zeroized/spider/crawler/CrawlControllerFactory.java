package com.zeroized.spider.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Zero on 2018/3/22.
 */
@Component
public class CrawlControllerFactory {
    @Value("${crawler.config.storage-dir}")
    private String baseDir;

    @Value("${crawler.config.default.workers}")
    private int workers;

    @Value("${crawler.config.default.dir}")
    private String dir;

    private boolean resumeable = false;

    @Value("${crawler.config.default.polite-wait}")
    private int delay;

    @Value("${crawler.config.default.max-depth}")
    private int depth;

    @Value("${crawler.config.default.max-page}")
    private int page;

    public CrawlControllerFactory() {
    }

    public CrawlControllerOptions createOption() {
        return new CrawlControllerOptions(workers, dir, resumeable, delay, depth, page);
    }

    public CrawlController newController(CrawlControllerOptions options) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(baseDir + options.getDir());
        config.setMaxDepthOfCrawling(options.getDepth());
        config.setPolitenessDelay(options.getDelay());
        config.setResumableCrawling(options.isResumeable());
        config.setDefaultHeaders(options.getHeaders());
        if (options.getPage() != -1) {
            config.setMaxPagesToFetch(options.getPage());
        }
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        return new CrawlController(config, pageFetcher, robotstxtServer);
    }
}

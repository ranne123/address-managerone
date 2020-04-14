package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.cloudplatform.cache.CacheKey;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.s4hana.connectivity.CachingErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import org.slf4j.Logger;
import rx.schedulers.Schedulers;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetAllBusinessPartnersCommand extends CachingErpCommand<List<BusinessPartner>> {
    private static final Logger logger = CloudLoggerFactory.getLogger(GetAllBusinessPartnersCommand.class);

    private static final String CATEGORY_PERSON = "1";

    private static final Cache<CacheKey, List<BusinessPartner>> cache = CacheBuilder.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private final BusinessPartnerService service;

    public GetAllBusinessPartnersCommand(final BusinessPartnerService service) {
        super(HystrixUtil.getDefaultErpCommandSetter(
                GetAllBusinessPartnersCommand.class,
                HystrixUtil.getDefaultErpCommandProperties().withExecutionTimeoutEnabled(false)
                .withExecutionTimeoutEnabled(true))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                        .withMaxQueueSize(25)
                        .withQueueSizeRejectionThreshold(26)
                        .withMaximumSize(40)
                        .withCoreSize(1)
        ));
                        //withExecutionTimeoutInMilliseconds(1000000000)));

        this.service = service;
    }

    @Override
    public Cache<CacheKey, List<BusinessPartner>> getCache() {
        return cache;
    }

    @Override
    protected List<BusinessPartner> runCacheable() throws Exception {
        final List<BusinessPartner> businessPartners = service
                .getAllBusinessPartner()
                .select(BusinessPartner.BUSINESS_PARTNER,
                        BusinessPartner.LAST_NAME,
                        BusinessPartner.FIRST_NAME)
                .filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq(CATEGORY_PERSON))
                .orderBy(BusinessPartner.LAST_NAME, Order.ASC)
                .execute();

        Schedulers.shutdown();
        Hystrix.reset();
        return businessPartners;
    }

    @Override
    protected List<BusinessPartner> getFallback() {
        logger.warn("Fallback called because of exception:", getExecutionException());
        return Collections.emptyList();
    }
    @PreDestroy
    public void shutdown() {
        Schedulers.shutdown();
        Hystrix.reset();
    }
}

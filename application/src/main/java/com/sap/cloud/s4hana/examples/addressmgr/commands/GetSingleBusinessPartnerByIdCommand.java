package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import org.slf4j.Logger;
import rx.schedulers.Schedulers;

//import javax.annotation.PreDestroy;

public class GetSingleBusinessPartnerByIdCommand extends ErpCommand<BusinessPartner> {
    private static final Logger logger = CloudLoggerFactory.getLogger(GetSingleBusinessPartnerByIdCommand.class);

    private final BusinessPartnerService service;
    private final String id;

    public GetSingleBusinessPartnerByIdCommand(final BusinessPartnerService service, final String id) {
        super(HystrixUtil.getDefaultErpCommandSetter(
                GetSingleBusinessPartnerByIdCommand.class,
                HystrixUtil.getDefaultErpCommandProperties().withExecutionTimeoutEnabled(false)
                .withExecutionTimeoutEnabled(true))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                        .withMaxQueueSize(25)
                        .withQueueSizeRejectionThreshold(26)
                        .withMaximumSize(40)
                        .withCoreSize(1)
        ));
                        //withExecutionTimeoutInMilliseconds(100000000)));

        this.service = service;
        this.id = id;
    }

    @Override
    protected BusinessPartner run() throws Exception {
        final BusinessPartner businessPartner = service
                .getBusinessPartnerByKey(id)
                .select(BusinessPartner.BUSINESS_PARTNER,
                        BusinessPartner.LAST_NAME,
                        BusinessPartner.FIRST_NAME,
                        BusinessPartner.IS_MALE,
                        BusinessPartner.IS_FEMALE,
                        BusinessPartner.CREATION_DATE,
                        BusinessPartner.TO_BUSINESS_PARTNER_ADDRESS.select(
                                BusinessPartnerAddress.BUSINESS_PARTNER,
                                BusinessPartnerAddress.ADDRESS_ID,
                                BusinessPartnerAddress.COUNTRY,
                                BusinessPartnerAddress.POSTAL_CODE,
                                BusinessPartnerAddress.CITY_NAME,
                                BusinessPartnerAddress.STREET_NAME,
                                BusinessPartnerAddress.HOUSE_NUMBER))
                .execute();
        Schedulers.shutdown();
        Hystrix.reset();
        return businessPartner;
    }

    @Override
    protected BusinessPartner getFallback() {
        logger.warn("Fallback called because of exception:", getExecutionException());
        return BusinessPartner.builder().businessPartner(id).build();
    }
   /* @PreDestroy
    public void shutdown() {
        Schedulers.shutdown();
        Hystrix.reset();
    }*/
}

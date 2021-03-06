package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixThreadPoolProperties;
//import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.odatav2.connectivity.ODataDeleteResult;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
//import org.slf4j.Logger;
import rx.schedulers.Schedulers;



public class DeleteAddressCommand extends ErpCommand<Integer> {
    // private static final Logger logger = CloudLoggerFactory.getLogger(DeleteAddressCommand.class);

    private final BusinessPartnerService service;
    private final String businessPartnerId;
    private final String addressId;

    public DeleteAddressCommand(final BusinessPartnerService service,
                                final String businessPartnerId, final String addressId) {
        super(HystrixUtil.getDefaultErpCommandSetter(
                DeleteAddressCommand.class,
                HystrixUtil.getDefaultErpCommandProperties().withExecutionTimeoutEnabled(false)
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(1000000000))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                        .withMaxQueueSize(25)
                        .withQueueSizeRejectionThreshold(26)
                        .withMaximumSize(40)
                        .withCoreSize(1)

                ));
        //withExecutionTimeoutInMilliseconds(1000000000)));

        this.service = service;
        this.businessPartnerId = businessPartnerId;
        this.addressId = addressId;
    }

    @Override
    protected Integer run() throws Exception {
        final BusinessPartnerAddress addressToDelete = BusinessPartnerAddress.builder()
                .businessPartner(businessPartnerId)
                .addressID(addressId)
                .build();

        final ODataDeleteResult oDataDeleteResult = service
                .deleteBusinessPartnerAddress(addressToDelete)
                .execute();


        Schedulers.shutdown();
        Hystrix.reset();
        return oDataDeleteResult.getHttpStatusCode();
    }

   /* @PreDestroy
    public void shutdown() {
        Schedulers.shutdown();
        Hystrix.reset();
    }*/
}

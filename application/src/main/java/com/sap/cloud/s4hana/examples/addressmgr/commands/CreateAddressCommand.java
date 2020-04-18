package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixThreadPoolProperties;

//import org.slf4j.Logger;

//import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import rx.schedulers.Schedulers;

import javax.annotation.PreDestroy;
import java.util.List;

public class CreateAddressCommand extends ErpCommand<BusinessPartnerAddress> {
    // private static final Logger logger = CloudLoggerFactory.getLogger(CreateAddressCommand.class);

    private final BusinessPartnerService service;
    private final BusinessPartnerAddress addressToCreate;

    public CreateAddressCommand(final BusinessPartnerService service, final BusinessPartnerAddress addressToCreate) {
        super(HystrixUtil.getDefaultErpCommandSetter(
                CreateAddressCommand.class,
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
        //  .withExecutionTimeoutInMilliseconds(1000000000)));

        this.service = service;
        this.addressToCreate = addressToCreate;
    }

    @Override
    protected BusinessPartnerAddress run() throws Exception {
        final BusinessPartnerAddress addressCreated = service
                .createBusinessPartnerAddress(addressToCreate)
                .execute();
        Schedulers.shutdown();
        Hystrix.reset();
        return addressCreated;
    }


   /* @PreDestroy
    public void shutdown() {
        Schedulers.shutdown();
        Hystrix.reset();
        try {
            if (ConfigurationManager.getConfigInstance() instanceof DynamicConfiguration) {
                DynamicConfiguration config = (DynamicConfiguration) ConfigurationManager.getConfigInstance();
                config.stopLoading();
            } else if (ConfigurationManager.getConfigInstance() instanceof ConcurrentCompositeConfiguration) {
                ConcurrentCompositeConfiguration configInst = (ConcurrentCompositeConfiguration) ConfigurationManager
                        .getConfigInstance();
                List configs = configInst.getConfigurations();
                if (configs != null) {
                    for (Object config : configs) {
                        if (config instanceof DynamicConfiguration) {
                            ((DynamicConfiguration) config).stopLoading();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

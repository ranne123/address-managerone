package com.sap.cloud.s4hana.examples.addressmgr.commands;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.odatav2.connectivity.ODataUpdateResult;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import org.apache.commons.configuration.AbstractConfiguration;
import org.slf4j.Logger;
import rx.schedulers.Schedulers;

import javax.annotation.PreDestroy;
import java.util.List;

public class UpdateAddressCommand extends ErpCommand<Integer> {
    private static final Logger logger = CloudLoggerFactory.getLogger(UpdateAddressCommand.class);

    private final BusinessPartnerService service;
    private final BusinessPartnerAddress addressToUpdate;

    public UpdateAddressCommand(final BusinessPartnerService service, final BusinessPartnerAddress addressToUpdate) {
        super(HystrixUtil.getDefaultErpCommandSetter(
                UpdateAddressCommand.class,
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
        this.addressToUpdate = addressToUpdate;
    }

    @Override
    protected Integer run() throws Exception {
        final ODataUpdateResult oDataUpdateResult = service
                .updateBusinessPartnerAddress(addressToUpdate)
                .execute();
        Schedulers.shutdown();
        Hystrix.reset();
        return oDataUpdateResult.getHttpStatusCode();
    }




    @PreDestroy
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
}
}

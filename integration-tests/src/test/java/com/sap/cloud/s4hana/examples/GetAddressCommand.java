package com.sap.cloud.s4hana.examples;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sap.cloud.sdk.frameworks.hystrix.HystrixUtil;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerAddress;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import rx.schedulers.Schedulers;

public class GetAddressCommand extends HystrixCommand<BusinessPartnerAddress> {

    private String bupaId;
    private String addressId;

    public GetAddressCommand(final String bupaId, final String addressId) {
        super(HystrixUtil
            .getDefaultErpCommandSetter(
                GetAddressCommand.class,
                HystrixUtil.getDefaultErpCommandProperties()

                        .withExecutionTimeoutEnabled(true))
                        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                .withAllowMaximumSizeToDivergeFromCoreSize(true)
                                .withMaxQueueSize(25)
                                .withQueueSizeRejectionThreshold(26)
                                .withMaximumSize(40)
                                .withCoreSize(1)
            ))
                ;
        this.bupaId = bupaId;
        this.addressId = addressId;

    }

    @Override
    protected BusinessPartnerAddress run() throws ODataException {
        try {
            return new DefaultBusinessPartnerService()
                    .getBusinessPartnerAddressByKey(bupaId, addressId)
                    .execute();

        } catch (ODataException e) {
            if (e.getCode().equals("404")) {
                return null;
            } else {
                throw e;
            }
        }
        finally {
          //Schedulers.shutdown();
            Hystrix.reset();
        }
    }
}

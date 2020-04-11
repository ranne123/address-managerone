package com.sap.cloud.s4hana.examples;

;

import com.google.common.collect.Lists;
import com.sap.cloud.s4hana.examples.addressmgr.commands.GetAllBusinessPartnersCommand;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerSelectable;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import com.sap.cloud.sdk.testutil.MockUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GetAllBusinessPartnersCommandTest {
    private MockUtil mockUtil;
    @Mock(answer = RETURNS_DEEP_STUBS)
    public BusinessPartnerService service;

    @Before
    public void beforeClass() {
        mockUtil = new MockUtil();
        mockUtil.mockDefaults(); //mocks the locale ,tenant etc
        mockUtil.mockDestination("ErpQueryEndpoint", URI.create(""));
        //Invalidate cache ahead of each
        new GetAllBusinessPartnersCommand(null).getCache().invalidateAll();
        ;
    }

    @Test
    public void testGetAll() throws ODataException {
      //  final BusinessPartnerService service = mock(BusinessPartnerService.class, RETURNS_DEEP_STUBS);
        BusinessPartner alice = new BusinessPartner();
        alice.setFirstName("Alice");
        alice.setLastName("Miller");

        //prepare the service
        when(service.getAllBusinessPartner()
                .select(any(BusinessPartnerSelectable.class))
                .filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq("1"))
                .orderBy(BusinessPartner.LAST_NAME, Order.ASC)
                .execute()
        ).thenReturn(Lists.newArrayList(alice));
        final List<BusinessPartner> result = new GetAllBusinessPartnersCommand(service).execute();

//assertions
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Miller", result.get(0).getLastName());
    }
}

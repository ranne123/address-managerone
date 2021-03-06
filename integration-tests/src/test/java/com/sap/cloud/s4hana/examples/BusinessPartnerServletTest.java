package com.sap.cloud.s4hana.examples;

import com.sap.cloud.s4hana.examples.addressmgr.BusinessPartnerServlet;
import com.sap.cloud.sdk.testutil.MockUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

@RunWith(Arquillian.class)
public class BusinessPartnerServletTest {
    public static final String BUPA_ID = "1003764";
    private static final MockUtil mockUtil = new MockUtil();
    @ArquillianResource
    private URL baseUrl;

    @Deployment
    public static WebArchive createDeployment(){
        return TestUtil.createDeployment(BusinessPartnerServlet.class);

    }
    @BeforeClass
    public static void beforeClass(){
        mockUtil.mockDefaults();
        mockUtil.mockErpDestination();
    }
    @Before
    public void Before(){
        RestAssured.baseURI = baseUrl.toExternalForm();
    }
    @Test
    public void testGetAll(){
        when()
                .get("/api/business-partners")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$",hasSize(greaterThan(1)))
                .body("[0].BusinessPartner",not(isEmptyOrNullString()));
    }
}
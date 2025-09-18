package com.redhat;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        System.out.println("🔌 Camel Routes starting up...");

        /*
        Kafka (JSON) -> Translate to XML -> Kafka
        */        
        from("kafka:{{kafka.package.receiver}}?groupId=camel-transformer")
            .routeId("PackageReceiverToDeliverer")
            .log("📦 Received package JSON: ${body}")
            // Transform JSON → XML (attached is an XSL for rules formatting - we have this customized specifically)
            .to("xj:com/redhat/json2xml.xsl?transformDirection=JSON2XML")
            .log("🚚 Transformed package to XML: ${body}")
            .to("kafka:{{kafka.package.deliverer}}")
            .log("✅ Delivered package to 'package-deliverer': ${body}");

        /*
        File Drop (JSON) -> Translate to XML -> Push to Kafka
        */
        from("minio:{{minio.bucket}}"
            + "?accessKey={{minio.accessKey}}"
            + "&secretKey={{minio.secretKey}}"
            + "&endpoint={{minio.endpoint}}"
            + "&secure=true")
        .routeId("MinioToKafka")
        .log("📂 Picked up from MinIO bucket: ${header.CamelFileName}")
        .to("xj:com/redhat/json2xml.xsl?transformDirection=JSON2XML")
        .to("kafka:package-deliverer")
        .log("✅ Delivered to Kafka: ${body}");            

        /*
         REST -> JSON in -> XML out (Note: using DVB's XSLT)
         */
        rest("/process")
            .post("/json2xml")
                .consumes("application/json")
                .produces("application/xml")
                .to("direct:json2xml");
        from("direct:json2xml")
            .routeId("JsonToXml")
            .log("📥 Received JSON: ${body}")
            // replace with your own XSLT or schema resource
            .to("xj:com/redhat/json2xml.xsl?transformDirection=JSON2XML")
            .log("📤 Returning XML: ${body}");        
    }
}
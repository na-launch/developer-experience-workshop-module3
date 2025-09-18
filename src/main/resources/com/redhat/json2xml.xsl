<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xj="http://camel.apache.org/component/xj"
                exclude-result-prefixes="xj">

    <xsl:output omit-xml-declaration="no" encoding="UTF-8" method="xml" indent="yes"/>

    <xsl:template match="/">
        <transaction>
            <xsl:apply-templates select="//object"/>
        </transaction>
    </xsl:template>

    <xsl:template match="object[@xj:type != 'object' and @xj:type != 'array' and string-length(@xj:name) > 0]">
        <xsl:variable name="name" select="@xj:name"/>
        <xsl:element name="{$name}">
            <xsl:value-of select="text()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="@*|node()"/>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template match="/html">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="content" 
					page-width="8.5in" page-height="11in" 
					margin-top="1in" margin-bottom=".5in" 
					margin-left=".75in" margin-right=".75in">
					
					<fo:region-body 
						margin-top=".8in" margin-bottom=".5in" 
						margin-left="0in" margin-right="0in"
					/>
					<fo:region-before extent='1.25in'/>
					<fo:region-after extent='.5in'/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			<fo:page-sequence master-reference="content">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-family='Helvetica' font-size='9pt'>
						<xsl:apply-templates select='body/*'/>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template match="div">
		<fo:block>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="h1">
		<fo:block font-size="16px" font-weight="bold">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="p">
		<fo:block space-before='6px' space-after='6px'>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="dl">
		<fo:block space-before='6px' space-after='6px'>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="dt">
		<fo:block space-before='3px' space-after='3px'>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="dd">
		<fo:block margin-left='0.5in'>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="label">
		<fo:inline font-weight='bold'>
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="a">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="form|input"/>
</xsl:stylesheet>
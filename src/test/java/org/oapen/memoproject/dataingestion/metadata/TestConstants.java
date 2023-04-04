package org.oapen.memoproject.dataingestion.metadata;

public class TestConstants {

	final static String onixrecord = 
			  "   <Product>"
			+ "      <RecordReference>OAPEN-ID_5fd15d8e-628e-4e16-9dd8-f1b07b37efa7</RecordReference>"
			+ "      <NotificationType>03</NotificationType>"
			+ "      <RecordSourceType>00</RecordSourceType>"
			+ "      <ProductIdentifier>"
			+ "         <ProductIDType>01</ProductIDType>"
			+ "         <IDTypeName>OAPEN Library ID</IDTypeName>"
			+ "         <IDValue>ONIX_20201001_9783653064650_196</IDValue>"
			+ "      </ProductIdentifier>"
			+ "      <ProductIdentifier>"
			+ "         <ProductIDType>15</ProductIDType>"
			+ "         <IDValue>9783653064650</IDValue>"
			+ "      </ProductIdentifier>"
			+ "      <ProductIdentifier>"
			+ "         <ProductIDType>06</ProductIDType>"
			+ "         <IDValue>10.3726/978-3-653-06465-0</IDValue>"
			+ "      </ProductIdentifier>"
			+ "      <DescriptiveDetail>"
			+ "      </DescriptiveDetail>"
			+ "      <CollateralDetail>"
			+ "         <SupportingResource>"
		    + "            <ResourceVersion>"
		    + "               <ResourceLink>https://library.oapen.org/bitstream/handle/20.500.12657/42289/9783653064650.pdf.jpg?sequence=3</ResourceLink>"
		    + "            </ResourceVersion>"
		    + "         </SupportingResource>"
		    + "      </CollateralDetail>"
			+ "      <PublishingDetail>"
			+ "      </PublishingDetail>"
			+ "      <RelatedMaterial>"
			+ "      </RelatedMaterial>"
			+ "      <ProductSupply>"
			+ "      </ProductSupply>"
			+ "   </Product>"
			;
	
	
	final static String marcxmlrecord = 
			  "   <marc:record>"
			+ "      <marc:leader>naaaa       uu</marc:leader>"
			+ "      <marc:controlfield tag=\"001\">https://library.oapen.org/handle/20.500.12657/42289</marc:controlfield>"
			+ "      <marc:controlfield tag=\"005\">20201001</marc:controlfield>"
			+ "      <marc:controlfield tag=\"003\">oapen</marc:controlfield>"
			+ "      <marc:datafield tag=\"020\" ind1=\" \" ind2=\" \"></marc:datafield>"
			+ "      <marc:datafield tag=\"040\" ind1=\" \" ind2=\" \"></marc:datafield>"
			+ "   </marc:record>"
			;
	
	
	final static String risrecord = 
			  "TY  - BOOK\n"
			+ "ED  - Swan Sik, Ko\n"
			+ "ED  - Syatauw, J.J.G.\n"
			+ "ED  - Pinto, M.C.W.\n"
			+ "AB  - The Asian Yearbook of International Law is a major refereed publication dedicated to international law issues as seen primarily from an Asian perspective. Readership: Academics and practitioners who deal with international public law in Asia will appreciate this unique, complete resource. The Asian Yearbook of International Law provides insight into Asian views and practices, especially for non-Asian readers, and also promotes the dissemination of knowledge of international law in Asia.\n"
			+ "DO  - 10.1163/9789004400658\n"
			+ "ID  - OAPEN ID: ONIX_20200515_9789004400658_11\n"
			+ "KW  - International law\n"
			+ "L1  - https://library.oapen.org/bitstream/id/b99b578d-97b0-4c91-90cb-65b745d919fa/9789004400658_webready_content_text.pdf\n"
			+ "LA  - English\n"
			+ "LK  - http://library.oapen.org/handle/20.500.12657/38065\n"
			+ "PB  - Brill\n"
			+ "PY  - 1998\n"
			+ "TI  - Asian Yearbook of International Law, Volume 6 (1996)\n"
			+ "ER  - \n"
			;
	
	final static String kbartrecord = 
			"Identitaetsbildung und Partizipation im 19. und 20. Jahrhundert : Luxemburg im europaeischen Kontext	9783653064650	10.3726/978-3-653-06465-0							https://library.oapen.org/bitstream/20.500.12657/42289/1/9783653064650.pdf		20.500.12657/42289		full text	Publication licence: https://creativecommons.org/licenses/by-nc-nd/4.0	Peter Lang International Academic Publishers	monograph	2016	2020-10-01T18:20:50Z	12		Franz, Norbert			F	\n"
			;
	
	
}

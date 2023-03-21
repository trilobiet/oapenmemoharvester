package org.oapen.memoproject.dataingestion.metadata;

public class TestConstants {

	final static String onixdoc = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<ONIXMessage xmlns=\"http://ns.editeur.org/onix/3.0/reference\" release=\"3.0\">"
			+ "   <Header>"
			+ "      <Sender>"
			+ "         <SenderName>OAPEN Foundation</SenderName>"
			+ "         <EmailAddress>info@oapen.org</EmailAddress>"
			+ "      </Sender>"
			+ "      <SentDateTime>20221221</SentDateTime>"
			+ "   </Header>"
			+ "   <Product>"
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
			+ "      </CollateralDetail>"
			+ "      <PublishingDetail>"
			+ "      </PublishingDetail>"
			+ "      <RelatedMaterial>"
			+ "      </RelatedMaterial>"
			+ "      <ProductSupply>"
			+ "      </ProductSupply>"
			+ "   </Product>"
			+ "   <Product>"
			+ "      <RecordReference>OAPEN-ID_42606b1b-876f-427d-b8ec-91f9bcaa2a4e</RecordReference>"
			+ "      <NotificationType>03</NotificationType>"
			+ "      <RecordSourceType>00</RecordSourceType>"
			+ "      <ProductIdentifier>"
			+ "         <ProductIDType>01</ProductIDType>"
			+ "         <IDTypeName>OAPEN Library ID</IDTypeName>"
			+ "         <IDValue>ONIX_20200527_9781912250134_11</IDValue>"
			+ "      </ProductIdentifier>"
			+ "      <ProductIdentifier>"
			+ "         <ProductIDType>15</ProductIDType>"
			+ "         <IDValue>9781912250134</IDValue>"
			+ "      </ProductIdentifier>"
			+ "      <ProductIdentifier>"
			+ "         <ProductIDType>06</ProductIDType>"
			+ "         <IDValue>10.14296/518.9781912250134</IDValue>"
			+ "      </ProductIdentifier>"
			+ "      <DescriptiveDetail>"
			+ "      </DescriptiveDetail>"
			+ "      <CollateralDetail>"
			+ "      </CollateralDetail>"
			+ "      <PublishingDetail>"
			+ "      </PublishingDetail>"
			+ "      <RelatedMaterial>"
			+ "      </RelatedMaterial>"
			+ "      <ProductSupply>"
			+ "      </ProductSupply>"
			+ "   </Product>"
			+ "</ONIXMessage>"
			;
	
	
	final static String marcxmldoc = 
			  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<marc:collection xmlns:marc=\"http://www.loc.gov/MARC21/slim\">"
			+ "    "
			+ "   <marc:record>"
			+ "      <marc:leader>naaaa       uu</marc:leader>"
			+ "      <marc:controlfield tag=\"001\">https://library.oapen.org/handle/20.500.12657/42289</marc:controlfield>"
			+ "      <marc:controlfield tag=\"005\">20201001</marc:controlfield>"
			+ "      <marc:controlfield tag=\"003\">oapen</marc:controlfield>"
			+ "      <marc:datafield tag=\"020\" ind1=\" \" ind2=\" \"></marc:datafield>"
			+ "      <marc:datafield tag=\"040\" ind1=\" \" ind2=\" \"></marc:datafield>"
			+ "   </marc:record>"
			+ "   "
			+ "   <marc:record>"
			+ "      <marc:leader>naaaa       uu</marc:leader>"
			+ "      <marc:controlfield tag=\"001\">http://library.oapen.org/handle/20.500.12657/39385</marc:controlfield>"
			+ "      <marc:controlfield tag=\"005\">20200527</marc:controlfield>"
			+ "      <marc:controlfield tag=\"003\">oapen</marc:controlfield>"
			+ "      <marc:datafield tag=\"020\" ind1=\" \" ind2=\" \"></marc:datafield>"
			+ "      <marc:datafield tag=\"040\" ind1=\" \" ind2=\" \"></marc:datafield>"
			+ "   </marc:record>"
			+ "   "
			+ "</marc:collection>"
			;
	
	
	final static String risdoc = 
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
			+ "\n"
			+ "TY  - BOOK\n"
			+ "ED  - Hagemann, Hannah-Lena\n"
			+ "ED  - Heidemann, Stefan\n"
			+ "AB  - To integrate the regions of the early Islamic Empire from Central Asia to North Africa, transregional and regional elites of various backgrounds were essential. The papers analyze elite groups, their structures and networks, within selected regions across geographical, religious and social boundaries. While each region seems to be different, certain common patterns of governance and interaction made the largest empire of Late Antiquity work.\n"
			+ "DO  - 10.1515/9783110669800\n"
			+ "ID  - OAPEN ID: Book_9783110669800_20200507_17\n"
			+ "KW  - Elites\n"
			+ "KW  - Early Islamic History\n"
			+ "KW  - Umayyads Abbasids\n"
			+ "L1  - https://library.oapen.org/bitstream/id/a811c66e-ba21-4a9b-9a01-dc117f6f2efb/9783110669800.pdf\n"
			+ "LA  - English\n"
			+ "LK  - http://library.oapen.org/handle/20.500.12657/37613\n"
			+ "PB  - De Gruyter\n"
			+ "PP  - Berlin/Boston\n"
			+ "PY  - 2020\n"
			+ "TI  - Transregional and Regional Elites – Connecting the Early Islamic Empire\n"
			+ "ER  - \n"
			+ "\n"
			+ "TY  - BOOK\n"
			+ "AU  - Moi, Ruben\n"
			+ "AB  - This book interprets the multifarious writing of the Irish-American word wizard, Paul Muldoon, who has been described by The Times Literary Supplement as ‘the most significant English-language poet born since the second World War’. Readership: All interested in poetry and writing from Ireland and the English-speaking world, and in the enigma of language.\n"
			+ "DO  - 10.1163/9789004355118\n"
			+ "ID  - OAPEN ID: ONIX_20200515_9789004355118_56\n"
			+ "KW  - Literature: history & criticism\n"
			+ "L1  - https://library.oapen.org/bitstream/id/e1f8a4e1-96c0-4804-b369-6329ae646dbc/9789004355118_webready_content_text.pdf\n"
			+ "LA  - English\n"
			+ "LK  - http://library.oapen.org/handle/20.500.12657/38110\n"
			+ "PB  - Brill\n"
			+ "PY  - 2020\n"
			+ "TI  - The Language of Paul Muldoon\n"
			+ "ER  - "
			;
	
	final static String kbartdoc = 
			  "publication_title	print_identifier	online_identifier	date_first_issue_online	num_first_vol_online	num_first_issue_online	date_last_issue_online	num_last_vol_online	num_last_issue_online	title_url	first_author	title_id	embargo_info	coverage_depth	notes	publisher_name	publication_type	date_monograph_published_print	date_monograph_published_online	monograph_volume	monograph_edition	first_editor	parent_publication_title_id	preceding_publication_title_id	access_type	OCLC_Control_Number\n"
			+ "Identitaetsbildung und Partizipation im 19. und 20. Jahrhundert : Luxemburg im europaeischen Kontext	9783653064650	10.3726/978-3-653-06465-0							https://library.oapen.org/bitstream/20.500.12657/42289/1/9783653064650.pdf		20.500.12657/42289		full text	Publication licence: https://creativecommons.org/licenses/by-nc-nd/4.0	Peter Lang International Academic Publishers	monograph	2016	2020-10-01T18:20:50Z	12		Franz, Norbert			F	\n"
			+ "Human Rights, Sexual Orientation and Gender Identity in The Commonwealth	9781912250134	10.14296/518.9781912250134							https://library.oapen.org/bitstream/20.500.12657/39385/1/9781912250134.pdf		20.500.12657/39385		full text	Publication licence: https://creativecommons.org/licenses/by-by-nc-nd/4.0/	University of London Press	monograph	2013	2020-05-27T16:45:00Z			Lennox, Corinne			F	\n"
			+ "Asian Yearbook of International Law, Volume 6 (1996)	9789004400658	10.1163/9789004400658							https://library.oapen.org/bitstream/20.500.12657/38065/1/9789004400658_webready_content_text.pdf		20.500.12657/38065		full text	Publication licence: https://creativecommons.org/licenses/by-nc/4.0/	Brill	monograph	1998	2020-05-15T20:01:31Z	6		Swan Sik, Ko			F	\n"
			+ "";
	
	
}
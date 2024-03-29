package org.oapen.memoproject.dataingestion.harvest;

final class TestConstants {
	
	final static String xmldocument1 = 
			
			    "<OAI-PMH>"
			  + "	<responseDate>2023-01-23T13:49:34Z</responseDate>"
			  + "	<request verb=\"ListRecords\" metadataPrefix=\"xoai\" from=\"2023-01-21T00:00:00Z\">http://library.oapen.org/oai/request</request>"
		 	  + "	<ListRecords>"
			  +	"		<record>"
			  + "			<header>"
			  + "				<identifier>oai:library.oapen.org:20.500.12657/60744</identifier>"
			  + "				<datestamp>2023-01-21T04:29:41Z</datestamp>"
			  + "				<setSpec>com_20.500.12657_5</setSpec>"
			  + "				<setSpec>col_20.500.12657_6</setSpec>"
			  + "			</header>"
			  + "			<metadata>"
			  + "				<metadata"
			  + "					xmlns=\"http://www.lyncode.com/xoai\""
			  + "					xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.lyncode.com/xoai http://www.lyncode.com/xsd/xoai.xsd\">"
			  + "				</metadata>"
			  + "			</metadata>"
			  + "		</record>"
			  + "		<record>"
			  + "			<header>"
			  + "				<identifier>oai:library.oapen.org:20.500.12657/60840</identifier>"
			  + "				<datestamp>2023-01-21T04:29:41Z</datestamp>"
			  + "				<setSpec>com_20.500.12657_5</setSpec>"
			  + "				<setSpec>col_20.500.12657_6</setSpec>"
			  + "			</header>"
			  + "			<metadata>"
			  + "				<metadata"
			  + "					xmlns=\"http://www.lyncode.com/xoai\""
			  + "					xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.lyncode.com/xoai http://www.lyncode.com/xsd/xoai.xsd\">"
			  + "				</metadata>"
			  + "			</metadata>"
			  + "		</record>"
			  + "		<record>"
			  + "			<header>"
			  + "				<identifier>oai:library.oapen.org:20.500.12657/60780</identifier>"
			  + "				<datestamp>2023-01-21T04:29:41Z</datestamp>"
			  + "				<setSpec>com_20.500.12657_5</setSpec>"
			  + "				<setSpec>col_20.500.12657_6</setSpec>"
			  + "			</header>"
			  + "			<metadata>"
			  + "				<metadata"
			  + "					xmlns=\"http://www.lyncode.com/xoai\""
			  + "					xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.lyncode.com/xoai http://www.lyncode.com/xsd/xoai.xsd\">"
			  + "				</metadata>"
			  + "			</metadata>"
			  + "		</record>"
			  + "		<record>"
			  + "			<header status=\"deleted\">"
			  + "				<identifier>oai:library.oapen.org:20.500.12657/44456</identifier>"
			  + "				<datestamp>2020-12-16T17:18:49Z</datestamp>"
			  + "				<setSpec>com_20.500.12657_45647</setSpec>"
			  + "				<setSpec>col_20.500.12657_45648</setSpec>"
			  + "			</header>"
			  + "		</record>"
			  + "	</ListRecords>"
			  + "</OAI-PMH>";

	
	
	final static String xmldocumentResumptionToken = 
			
		    "<OAI-PMH>"
		  + "	<responseDate>2023-01-23T13:49:34Z</responseDate>"
		  + "	<request verb=\"ListRecords\" metadataPrefix=\"xoai\" from=\"2023-01-21T00:00:00Z\">http://library.oapen.org/oai/request</request>"
	 	  + "	<ListRecords>"
		  +	"		<record>"
		  + "			<header>"
		  + "				<identifier>oai:library.oapen.org:20.500.12657/60744</identifier>"
		  + "				<datestamp>2023-01-21T04:29:41Z</datestamp>"
		  + "				<setSpec>com_20.500.12657_5</setSpec>"
		  + "				<setSpec>col_20.500.12657_6</setSpec>"
		  + "			</header>"
		  + "			<metadata>"
		  + "				<metadata"
		  + "					xmlns=\"http://www.lyncode.com/xoai\""
		  + "					xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.lyncode.com/xoai http://www.lyncode.com/xsd/xoai.xsd\">"
		  + "				</metadata>"
		  + "			</metadata>"
		  + "		</record>"
		  + "		<record>"
		  + "			<header status=\"deleted\">"
		  + "				<identifier>oai:library.oapen.org:20.500.12657/44456</identifier>"
		  + "				<datestamp>2020-12-16T17:18:49Z</datestamp>"
		  + "				<setSpec>com_20.500.12657_45647</setSpec>"
		  + "				<setSpec>col_20.500.12657_45648</setSpec>"
		  + "			</header>"
		  + "		</record>"
		  + "		<resumptionToken completeListSize=\"161\" cursor=\"0\">xoai/2023-02-14T00:00:00Z///100</resumptionToken>"
		  + "	</ListRecords>"
		  + "</OAI-PMH>";
	
	
	final static String xmlrecord1 = 
  			"<record>"
		  + "	<header>"
		  + "		<identifier>oai:library.oapen.org:20.500.12657/60840</identifier>"
		  + "		<datestamp>2023-01-21T04:29:41Z</datestamp>"
		  + "		<setSpec>com_20.500.12657_5</setSpec>"
		  + "		<setSpec>col_20.500.12657_6</setSpec>"
		  + "	</header>"
		  + "	<metadata>"
		  + "		<metadata"
		  + "			xmlns=\"http://www.lyncode.com/xoai\""
		  + "			xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.lyncode.com/xoai http://www.lyncode.com/xsd/xoai.xsd\">"
		  + "			<element name=\"dc\">"
		  + "				<element name=\"contributor\">"
		  + "					<element name=\"author\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Gerritsen, Gerrit</field>"
		  + "							<field name=\"authority\">19380db3-36e6-41d5-a660-14ddb9cda0cc</field>"
		  + "							<field name=\"confidence\">300</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"editor\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Bartz, Eva</field>"
		  + "							<field name=\"authority\">533df1e9-441b-4750-9392-692b6e311b59</field>"
		  + "							<field name=\"confidence\">300</field>"
		  + "							<field name=\"value\">Bartz-Beielstein, Thomas</field>"
		  + "							<field name=\"authority\">3202ec22-3d2a-4191-bddf-1bc133f71209</field>"
		  + "							<field name=\"confidence\">300</field>"
		  + "							<field name=\"value\">Zaefferer, Martin</field>"
		  + "							<field name=\"authority\">2bd53f1d-81f2-4a32-880d-decd73ecd84f</field>"
		  + "							<field name=\"confidence\">300</field>"
		  + "							<field name=\"value\">Mersmann, Olaf</field>"
		  + "							<field name=\"authority\">47c47812-f4cd-480d-857d-39090c6b8974</field>"
		  + "							<field name=\"confidence\">300</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"date\">"
		  + "					<element name=\"accessioned\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">2019-12-10 14:46:32</field>"
		  + "							<field name=\"value\">2010-12-31 23:55:55</field>"
		  + "							<field name=\"value\">2020-04-01T15:37:45Z</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"available\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">2023-01-20T16:54:39Z</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"issued\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">2023</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"identifier\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">340049</field>"
		  + "					</element>"
		  + "					<element name=\"issn\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">2612-8020</field>"
		  + "						</element>"
		  + "					</element>							"
		  + "					<element name=\"en_US\">"
		  + "						<field name=\"value\">OCN: 630533012</field>"
		  + "						<field name=\"value\">647791994</field>"
		  + "					</element>"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">ONIX_20230120_9789811951701_42</field>"
		  + "					</element>"
		  + "					<element name=\"uri\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">https://library.oapen.org/handle/20.500.12657/60840</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"description\">"
		  + "					<element name=\"abstract\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">This open access book provides a wealth of hands-on examples that illustrate how hyperparameter tuning can be applied in practice and gives deep insights into the working mechanisms of machine learning (ML) and deep learning (DL) methods. The aim of the book is to equip readers with the ability to achieve better results with significantly less time, costs, effort and resources using the methods described here. The case studies presented in this book can be run on a regular desktop or notebook computer. No high-performance computing facilities are required. The idea for the book originated in a study conducted by Bartz &amp; Bartz GmbH for the Federal Statistical Office of Germany (Destatis). Building on that study, the book is addressed to practitioners in industry as well as researchers, teachers and students in academia. The content focuses on the hyperparameter tuning of ML and DL algorithms, and is divided into two main parts: theory (Part I) and application (Part II). Essential topics covered include: a survey of important model parameters; four parameter tuning studies and one extensive global parameter tuning study; statistical analysis of the performance of ML and DL methods based on severity; and a new, consensus-ranking-based way to aggregate and analyze results from multiple algorithms. The book presents analyses of more than 30 hyperparameters from six relevant ML and DL methods, and provides source code so that users can reproduce the results. Accordingly, it serves as a handbook and textbook alike.</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"language\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"originalValue\">French</field>"
		  + "						<field name=\"value\">fre</field>"
		  + "						<field name=\"originalValue\">English</field>"
		  + "						<field name=\"value\">eng</field>"
		  + "						<field name=\"originalValue\">German</field>"
		  + "						<field name=\"value\">ger</field>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"relation\">"
		  + "					<element name=\"ispartofseries\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Cultural Heritage Studies</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"subject\">"
		  + "					<element name=\"classification\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">bic Book Industry Communication::U Computing &amp; information technology::UY Computer science::UYQ Artificial intelligence</field>"
		  + "							<field name=\"value\">bic Book Industry Communication::U Computing &amp; information technology::UY Computer science::UYQ Artificial intelligence::UYQM Machine learning</field>"
		  + "							<field name=\"value\">bic Book Industry Communication::U Computing &amp; information technology::UF Business applications::UFM Mathematical &amp; statistical software</field>"
		  + "							<field name=\"value\">bic Book Industry Communication::P Mathematics &amp; science::PH Physics::PHU Mathematical physics</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"other\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Hyperparameter Tuning</field>"
		  + "							<field name=\"value\">Hyperparameters</field>"
		  + "							<field name=\"value\">Tuning</field>"
		  + "							<field name=\"value\">Deep Neural Networks</field>"
		  + "							<field name=\"value\">Reinforcement Learning</field>"
		  + "							<field name=\"value\">Machine Learning</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"title\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">Hyperparameter Tuning for Machine and Deep Learning with R</field>"
		  + "					</element>"
		  + "					<element name=\"alternative\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">A Practical Guide</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"type\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">book</field>"
		  + "					</element>"
		  + "				</element>"
		  + "			</element>"
		  + "			<element name=\"dcterms\">"
		  + "				<element name=\"abstract\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">Simplified Signs presents a system of manual sign communication intended for special populations who have had limited success mastering spoken or full sign languages. It is the culmination of over twenty years of research and development by the authors. The Simplified Sign System has been developed and tested for ease of sign comprehension, memorization, and formation by limiting the complexity of the motor skills required to form each sign, and by ensuring that each sign visually resembles the meaning it conveys.Volume 1 outlines the research underpinning and informing the project, and places the Simplified Sign System in a wider context of sign usage, historically and by different populations. Volume 2 presents the lexicon of signs, totalling approximately 1000 signs, each with a clear illustration and a written description of how the sign is formed, as well as a memory aid that connects the sign visually to the meaning that it conveys. While the Simplified Sign System originally was developed to meet the needs of persons with intellectual disabilities, cerebral palsy, autism, or aphasia, it may also assist the communication needs of a wider audience – such as healthcare professionals, aid workers, military personnel , travellers or parents, and children who have not yet mastered spoken language.  The system also has been shown to enhance learning for individuals studying a foreign language. Lucid and comprehensive, this work constitutes a valuable resource that will enhance the communicative interactions of many different people, and will be of great interest to researchers and educators alike.</field>"
		  + "					</element>"
		  + "				</element>"
		  + "			</element>"
		  + "			<element name=\"oapen\">"
		  + "				<element name=\"abstract\">"
		  + "					<element name=\"otherlanguage\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Während der Zeit des historischen Kolonialismus wurden in Völkerkundemuseen komplexe Formen rassistischer und religiöser Diskriminierung institutionalisiert, z.B. in den dort gültigen Ästhetik- und Kunstbegriffen. Viele der heutigen Museumsangestellten erklären sich deswegen zu Reformen bereit. Doch können sie sich tatsächlich vom Kolonialismus trennen? Ist eine Dekolonisation ethnologischer Museen mit kolonialer Beute je abschließend möglich? Am Beispiel umstrittener Heiligtümer lebender Kulturen untersucht Christoph Balzar das Verfahren der Musealisierung durch die Linse der Diskriminierungskritik. Im Fokus stehen dabei die Sammlungen der »Staatlichen Museen zu Berlin«.</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"identifier\">"
		  + "					<element name=\"doi\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">10.1007/978-981-19-5170-1</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"ocn\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">630533012</field>"
		  + "							<field name=\"value\">647791994</field>"
		  + "						</element>"
		  + "					</element>	"
		  + "				</element>"
		  + "				<element name=\"relation\">"
		  + "					<element name=\"isPublishedBy\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">6c6992af-b843-4f46-859c-f6e9998e40d5</field>"
		  + "							<field name=\"authority\">6c6992af-b843-4f46-859c-f6e9998e40d5</field>"
		  + "							<field name=\"confidence\">600</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"isFundedBy\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">1b71b6aa-ef3b-4897-864f-0f1da4cd2438</field>"
		  + "							<field name=\"authority\">1b71b6aa-ef3b-4897-864f-0f1da4cd2438</field>"
		  + "							<field name=\"confidence\">600</field>"
		  + "						</element>"
		  + "					</element>"
		  + "					<element name=\"isbn\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">1000000000000;2000000000000;3000000000000</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"collection\">"
		  + "				  <element name=\"none\">"
		  + "				    <field name=\"value\">Toward an Open Monograph Ecosystem (TOME)</field>"
		  + "				    <field name=\"value\">Knowledge Unlatched (KU)</field>"
		  + "				  </element>"
		  + "				</element>"
		  + "				<element name=\"description\">"
		  + "					<element name=\"otherlanguage\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Just a test string</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>	"
		  + "				<element name=\"imprint\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">Springer Nature Singapore</field>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"pages\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">323</field>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"series\">"
		  + "					<element name=\"number\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">12345</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"chapternumber\">"
		  + "					<element name=\"none\">"
		  + "						<field name=\"value\">23</field>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"place\">"
		  + "					<element name=\"publication\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">Singapore</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"grant\">"
		  + "					<element name=\"number\">"
		  + "						<element name=\"none\">"
		  + "							<field name=\"value\">[...]</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "			</element>"
		  + "			<element name=\"bundles\">"
		  + "				<element name=\"bundle\">"
		  + "					<field name=\"name\">ORIGINAL</field>"
		  + "					<element name=\"bitstreams\">"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">978-981-19-5170-1.pdf</field>"
		  + "							<field name=\"originalName\">978-981-19-5170-1.pdf</field>"
		  + "							<field name=\"format\">application/pdf</field>"
		  + "							<field name=\"size\">4568524</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/1/978-981-19-5170-1.pdf</field>"
		  + "							<field name=\"checksum\">dfd9b3f9739a1ea8d6305af956d1fb86</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">1</field>"
		  + "							<field name=\"dctitle\">978-981-19-5170-1.pdf</field>"
		  + "							<field name=\"oapenrelationisbn\">9789811951701</field>"
		  + "							<field name=\"dcidentifierurlwebshop\">https://link.springer.com/978-981-19-5170-1</field>"
		  + "							<field name=\"rightsuri\">http://creativecommons.org/licenses/by/4.0/</field>"
		  + "							<field name=\"rights\">CC-BY</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"bundle\">"
		  + "					<field name=\"name\">EXPORT</field>"
		  + "					<element name=\"bitstreams\">"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">0b38dea2-8c42-451d-9744-20358c03f57b.marc.xml</field>"
		  + "							<field name=\"format\">application/octet-stream</field>"
		  + "							<field name=\"size\">8664</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/2/0b38dea2-8c42-451d-9744-20358c03f57b.marc.xml</field>"
		  + "							<field name=\"checksum\">d9ee5c650e93f77a23fd5181e2edbf05</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">2</field>"
		  + "							<field name=\"dctitle\">0b38dea2-8c42-451d-9744-20358c03f57b.marc.xml</field>"
		  + "						</element>"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">0b38dea2-8c42-451d-9744-20358c03f57b.onix_3.0.xml</field>"
		  + "							<field name=\"format\">application/octet-stream</field>"
		  + "							<field name=\"size\">7353</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/3/0b38dea2-8c42-451d-9744-20358c03f57b.onix_3.0.xml</field>"
		  + "							<field name=\"checksum\">1052fc1ba184f5edaa3e7c481af434fb</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">3</field>"
		  + "							<field name=\"dctitle\">0b38dea2-8c42-451d-9744-20358c03f57b.onix_3.0.xml</field>"
		  + "						</element>"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">0b38dea2-8c42-451d-9744-20358c03f57b.ris</field>"
		  + "							<field name=\"format\">application/octet-stream</field>"
		  + "							<field name=\"size\">2202</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/4/0b38dea2-8c42-451d-9744-20358c03f57b.ris</field>"
		  + "							<field name=\"checksum\">62749fad73f54c626ef449573b5aa7e9</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">4</field>"
		  + "							<field name=\"dctitle\">0b38dea2-8c42-451d-9744-20358c03f57b.ris</field>"
		  + "						</element>"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">0b38dea2-8c42-451d-9744-20358c03f57b.tsv</field>"
		  + "							<field name=\"format\">application/octet-stream</field>"
		  + "							<field name=\"size\">436</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/5/0b38dea2-8c42-451d-9744-20358c03f57b.tsv</field>"
		  + "							<field name=\"checksum\">ab6335f207c126fc7f71bb6266634312</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">5</field>"
		  + "							<field name=\"dctitle\">0b38dea2-8c42-451d-9744-20358c03f57b.tsv</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"bundle\">"
		  + "					<field name=\"name\">TEXT</field>"
		  + "					<element name=\"bitstreams\">"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">978-981-19-5170-1.pdf.txt</field>"
		  + "							<field name=\"originalName\">978-981-19-5170-1.pdf.txt</field>"
		  + "							<field name=\"description\">Extracted text</field>"
		  + "							<field name=\"format\">text/plain</field>"
		  + "							<field name=\"size\">620614</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/6/978-981-19-5170-1.pdf.txt</field>"
		  + "							<field name=\"checksum\">159dc9de7d16dbbc385a42d86c955474</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">6</field>"
		  + "							<field name=\"dctitle\">978-981-19-5170-1.pdf.txt</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"bundle\">"
		  + "					<field name=\"name\">THUMBNAIL</field>"
		  + "					<element name=\"bitstreams\">"
		  + "						<element name=\"bitstream\">"
		  + "							<field name=\"name\">978-981-19-5170-1.pdf.jpg</field>"
		  + "							<field name=\"originalName\">978-981-19-5170-1.pdf.jpg</field>"
		  + "							<field name=\"description\">Generated Thumbnail</field>"
		  + "							<field name=\"format\">image/jpeg</field>"
		  + "							<field name=\"size\">6731</field>"
		  + "							<field name=\"url\">https://library.oapen.org/bitstream/20.500.12657/60840/7/978-981-19-5170-1.pdf.jpg</field>"
		  + "							<field name=\"checksum\">5e25471fdf5e516a7b750e6b1eea92f8</field>"
		  + "							<field name=\"checksumAlgorithm\">MD5</field>"
		  + "							<field name=\"sid\">7</field>"
		  + "							<field name=\"dctitle\">978-981-19-5170-1.pdf.jpg</field>"
		  + "						</element>"
		  + "					</element>"
		  + "				</element>"
		  + "			</element>"
		  + "			<element name=\"others\">"
		  + "				<field name=\"uuid\">22222222-3aff-4e7b-9a1c-ce8c75fa9530</field>"
		  + "				<field name=\"handle\">20.500.12657/60840</field>"
		  + "				<field name=\"identifier\">oai:library.oapen.org:20.500.12657/60840</field>"
		  + "				<field name=\"lastModifyDate\">2023-01-21 04:29:41.338</field>"
		  + "			</element>"
		  + "			<element name=\"repository\">"
		  + "				<field name=\"name\">OAPEN Library</field>"
		  + "				<field name=\"mail\">info@oapen.org</field>"
		  + "			</element>"
		  + "			<element name=\"linkedItemsMetadata\">"
		  + "	            <element name=\"oapen.relation.isPartOfBook\">"
		  + "		            <field name=\"uuid\">e39d422d-dc90-47a3-8a26-cb55259c943b</field>"
		  + "		            <field name=\"handle\">20.500.12657/48278</field>"
		  + "		            <element name=\"dc.title\">"
		  + "			            <field name=\"value\">Knots</field>"
		  + "		            </element>"
		  + "	            </element>					"
		  + "				<element name=\"oapen.relation.isFundedBy\">"
		  + "					<field name=\"uuid\">1b71b6aa-ef3b-4897-864f-0f1da4cd2438</field>"
		  + "					<field name=\"handle\">20.500.12657/60839</field>"
		  + "					<element name=\"grantor.name\">"
		  + "						<field name=\"value\"> Austrian Science Fund </field>"
		  + "					</element>"
		  + "					<element name=\"grantor.program\"/>"
		  + "					<element name=\"grantor.number\"/>"
		  + "					<element name=\"grantor.acronym\">"
		  + "						<field name=\"value\">Fonds zur Förderung der Wissenschaftlichen Forschung</field>"
		  + "						<field name=\"value\">FWF Der Wissenschaftsfonds</field>"
		  + "						<field name=\"value\">FWF Austrian Science Fund</field>"
		  + "						<field name=\"value\">FWF</field>"
		  + "						<field name=\"value\">FFWF</field>"
		  + "					</element>"
		  + "					<element name=\"grantor.project\"/>"
		  + "					<element name=\"grantor.identifier.fundref\"/></element>"
		  + "				<element name=\"oapen.relation.isFundedBy\">"
		  + "					<field name=\"uuid\">1b71b6aa-ef3b-4897-864f-0f1da4cd2439</field>"
		  + "					<field name=\"handle\">20.500.12657/61833</field>"
		  + "					<element name=\"grantor.name\">"
		  + "						<field name=\"value\">Klaas</field>"
		  + "					</element>"
		  + "					<element name=\"grantor.program\">Program 1</element>"
		  + "					<element name=\"grantor.number\"/>"
		  + "					<element name=\"grantor.acronym\">"
		  + "						<field name=\"value\">K.L.S.</field>"
		  + "						<field name=\"value\">K.L.X.A.S.</field>"
		  + "						<field name=\"value\">K.L.S.</field>"
		  + "					</element>"
		  + "					<element name=\"grantor.project\"/>"
		  + "					<element name=\"grantor.identifier.fundref\"/></element>"
		  + "				<element name=\"oapen.relation.isPublishedBy\">"
		  + "					<field name=\"uuid\">6c6992af-b843-4f46-859c-f6e9998e40d5</field>"
		  + "					<field name=\"handle\">20.500.12657/22488</field>"
		  + "					<element name=\"publisher.name\">"
		  + "						<field name=\"value\">Springer Nature </field>"
		  + "					</element>"
		  + "				</element>"
		  + "			</element>"
		  + "			<element name=\"fieldsWithParentData\">"
		  + "				<element name=\"oapen.grant.number\">"
		  + "					<element name=\"parentInfo\">"
		  + "						<field name=\"parentField\">oapen.relation.isFundedBy</field>"
		  + "						<field name=\"parentValue\">1b71b6aa-ef3b-4897-864f-0f1da4cd2438</field>"
		  + "						<field name=\"originalValue\">10BP12_185527</field>"
		  + "					</element>"
		  + "				</element>"
		  + "				<element name=\"oapen.grant.program\">"
		  + "					<element name=\"parentInfo\">"
		  + "						<field name=\"parentField\">oapen.relation.isFundedBy</field>"
		  + "						<field name=\"parentValue\">1b71b6aa-ef3b-4897-864f-0f1da4cd2438</field>"
		  + "						<field name=\"originalValue\">Open Access Books</field>"
		  + "					</element>"
		  + "				</element>"
		  + "			</element>"
		  + "		</metadata>"
		  + "	</metadata>"
		  + "</record>";


	final static String xmlrecordDelete = 
		  "<record>"
		+ "		<header status=\"deleted\">"
		+ "			<identifier>oai:library.oapen.org:20.500.12657/44456</identifier>"
		+ "			<datestamp>2020-12-16T17:18:49Z</datestamp>"
		+ "			<setSpec>com_20.500.12657_45647</setSpec>"
		+ "			<setSpec>col_20.500.12657_45648</setSpec>"
		+ "		</header>"
		+ "</record>";	
		
	}

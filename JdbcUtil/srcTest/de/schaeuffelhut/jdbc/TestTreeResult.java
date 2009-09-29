/**
 * Copyright 2009 Friedrich Schäuffelhut
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package de.schaeuffelhut.jdbc;




/**
 * @author M.Sc. Friedrich Schäuffelhut
 *
 */
public class TestTreeResult
{
//	private static Connection connection;
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception
//	{
//		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		connection = DriverManager.getConnection("jdbc:mysql://localhost/jdbcutil",
//	        "jdbcutil", "jdbcutil");
//		
//		connection.createStatement().execute(
//				"DROP TABLE IF EXISTS unternehmen"
//		);
//		connection.createStatement().execute(
//				"CREATE TABLE unternehmen (" +
//                " id int primary key, " +
//				" name CHAR(30)" +
//				")"
//		);
//
//		connection.createStatement().execute(
//				"DROP TABLE IF EXISTS abteilung"
//		);
//		connection.createStatement().execute(
//				"CREATE TABLE abteilung (" +
//                " id int primary key, " +
//                " unternehmen int, " +
//				" name CHAR(30)" +
//				")"
//		);
//
//
//		connection.createStatement().execute(
//				"DROP TABLE IF EXISTS person"
//		);
//		connection.createStatement().execute(
//				"CREATE TABLE person (" +
//                " id int primary key, " +
//                " abteilung int, " +
//				" name CHAR(30)" +
//				")"
//		);
//
//
//		connection.createStatement().execute(
//				"INSERT INTO unternehmen (id, name)" +
//				" VALUES " +
//				"(1, 'Datapat GmbH')," +
//				"(2, 'PHP Technologies')," +
//				"(3, 'Hausleiter & Co')"
//		);
//
//		connection.createStatement().execute(
//				"INSERT INTO abteilung (id, unternehmen, name)" +
//				" VALUES " +
//				"(1, 1,'R&D')," +
//				"(2, 1,'Buchhaltung')," +
//				"(3, 2, 'Organisatzion')," +
//				"(4, 2, 'Entwicklung')," +
//				"(5, 3, 'Geschäftsführung')," +
//				"(6, 3, 'Vertrieb')"
//		);
//
//		connection.createStatement().execute(
//				"INSERT INTO person (id, abteilung, name)" +
//				" VALUES " +
//				"(1, 1,'Peter Betzler')," +
//				"(2, 2,'Katja Betzler')," +
//				"(3, 3, 'Phillip Pfitzenmaier')," +
//				"(4, 4, 'Friedrich Schäuffelhut')," +
//				"(5, 4, 'Rolf Weinzierl')," +
//				"(6, 5, 'Dr. Schäuffelhut')," +
//				"(7, 5, 'Stellverterter Schäuffelhut')," +
//				"(8, 6, 'Mitarbeiter A')," +
//				"(9, 6, 'Mitarbeiter B')," +
//				"(10, 6, 'Mitarbeiter C')"
//		);
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception
//	{
//		connection.close();
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception
//	{
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception
//	{
//	}
//
//	@Test
//	public void testConnection() throws Exception
//	{
//		Assert.assertNotNull( connection );
//	}
//
//	
//	@Test
//	public void testTreeResult() throws Exception
//	{
//		PreparedStatement stmt = connection.prepareStatement(
//				"SELECT u.id, u.name," +
//				"       a.id, a.unternehmen, a.name," +
//				"       p.id, p.abteilung, p.name" +
//				"  FROM unternehmen u JOIN abteilung a" +
//				"                       ON u.id = a.unternehmen" +
//				"                     JOIN person p" +
//				"                       ON a.id = p.abteilung"
//		);
//		ResultSet rs = stmt.executeQuery();
//		TreeResult readTreeResult = ResultSetUtil.readTreeResults(
//				rs,
//				ResultSetUtil.createResultReader(
//						ResultFactories.HashMapResultFactory,
//						ResultAdaptors.createMapResultAdaptors(
//								rs, 
//								1,
//								ResultTypes.mappedUUID( "unternehmen", ResultTypes.Long ),
//								ResultTypes.String
//						)
//				),
//				ResultSetUtil.createResultReader(
//						ResultFactories.HashMapResultFactory,
//						ResultAdaptors.createMapResultAdaptors(
//								rs,
//								3,
//								ResultTypes.mappedUUID( "abteilung", ResultTypes.Long ),
//								ResultTypes.mappedUUID( "unternehmen", ResultTypes.Long ),
//								ResultTypes.String
//						)
//				),
//				ResultSetUtil.createResultReader(
//						ResultFactories.HashMapResultFactory,
//						ResultAdaptors.createMapResultAdaptors(
//								rs,
//								6,
//								ResultTypes.mappedUUID( "person", ResultTypes.Long ),
//								ResultTypes.mappedUUID( "abteilung", ResultTypes.Long ),
//								ResultTypes.String
//						)
//				)
//		);
//		List<Object> nodes = readTreeResult.rootNodes();
//		show( "", readTreeResult, nodes );
//	}
//
//	private void show(String spaces, TreeResult readTreeResult, List<Object> nodes)
//	{
//		if ( nodes == null )
//			return;
//		for(Object o : nodes )
//		{
//			Map m = (Map)o;
//			System.err.println( spaces + m.get( "name" ) + "," + MappedUUIDResultType.reverse( (UUID)m.get("id") ) + " => " + readTreeResult.children( o ) );
//			show( spaces + "  ", readTreeResult, readTreeResult.children( o ) );
//		}
//	}
}

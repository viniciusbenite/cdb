# Vinicius Ribeiro
# Nmec 82773
# Make sure to run pip3 install -r requirements.txt and load the .dump at Neo4j
# https://neo4j.com/docs/operations-manual/current/tools/dump-load/
# Dataset: https://neo4j.com/graphgist/beer-amp-breweries-graphgist#_create_nodes_and_relationships

import sys

from neo4j import GraphDatabase


# Connect to local DB
def init_db(uri, user, password):
    try:
        _driver = GraphDatabase.driver(uri, auth=(user, password))
        print("Connection successful")
    except:
        print("Something went wrong...")
        sys.exit()
    with _driver.session() as session:
        result = session.run("match (c) return c")
        result = list(result)
        print("rows: {}".format(len(result)))

        exec_queries(session)



def exec_queries(session):
    # Map of queries to be executed:
    queries = {"querie1": "MATCH (b:Beer)-[r:BEER_CATEGORY]->(c:Category{category:'British Ale'})"
                          "RETURN b AS BEER, c AS TYPE",

               "querie2": "MATCH (b:Beer)-[r:BEER_STYLE]->(s:Style)"
                          "WHERE s.style CONTAINS 'Ale'"
                          "RETURN b.name AS BEER, s AS STYLE",

               "querie3": "MATCH (category:Category{category: 'British Ale'})<-[:BEER_CATEGORY]-(beer:Beer)-["
                          ":BREWED_AT]->(brewery: Brewery)-[:LOC_CITY]->(city:City)-[:LOC_STATE]->(state:State)-["
                          ":LOC_COUNTRY]->(country:Country {country: 'United Kingdom'}) "
                          "RETURN Distinct(beer.name) as beer,  brewery.name as brewery, city.city as city "
                          "ORDER BY city, beer",

               "querie4": "MATCH (b:Beer)-[r:BEER_CATEGORY]->(c:Category {category: 'Irish Ale'})"
                          "RETURN b.name AS BEER_NAME, b.abv AS ALC ,c.category AS CATEGORY "
                          "ORDER BY b.abv DESC "
                          "LIMIT 10",

               "querie5": "MATCH (b:Beer)"
                          "WHERE b.name CONTAINS 'Stout'"
                          "RETURN b.name AS BEER "
                          "ORDER BY b.name",

               "querie6": "MATCH (b:Beer)-[:BEER_STYLE]->(s:Style)"
                          "WHERE b.name CONTAINS 'IPA' AND s.style CONTAINS 'Ale'"
                          "RETURN b.name AS BEER, s.style AS STYLE",

               "querie7": "MATCH (b:Beer)-[:BREWED_AT]->(brewery: Brewery)-[:LOC_CITY]->(city:City)-[:LOC_STATE]->("
                          "state:State)-[:LOC_COUNTRY]->(country:Country) "
                          "WHERE country.country <> 'Belgium' AND b.abv > 9.0 "
                          "RETURN DISTINCT b.name AS BEER, country.country AS COUNTRY, b.abv AS ALCH "
                          "ORDER BY b.abv",

               "querie8": "MATCH (b:Beer)-[:BEER_STYLE]->(s:Style)"
                          "WITH s, COUNT(b) AS total "
                          "RETURN s.style AS STYLE , total AS NUM_OF_DIF_BEERS "
                          "ORDER BY total DESC "
                          "LIMIT 10",

               "querie9": "MATCH path=shortestPath((b1:Beer)-[*]-(b2:Beer)) "
                          "WHERE b1.name='Bare Knuckle Stout' AND  b2.name='Hop Ottin IPA' "
                          "RETURN length(path) AS PATH_LEN, path AS PATH ",

               "querie10": "MATCH path=shortestPath((b1:Beer)-[*]-(b2:Beer)) "
                           "WHERE b1.name <> b2.name "
                           "RETURN LENGTH(path) AS LENGTH, b1.name AS BEER1, b2.name AS BEER2, path AS PATH "
                           "ORDER BY LENGTH DESC "
                           "LIMIT 1"}

    for key in queries:
            results = session.run(queries[key])
            for result in results:
                print(result)
    close(session)
    print("FINISH!")


def close(_driver):
    _driver.close()


init_db("bolt://localhost:7687", "neo4j", "12345")

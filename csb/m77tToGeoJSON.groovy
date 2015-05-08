import groovy.json.JsonBuilder

def id
def lat
def lon
def z
def tokens = []
def rows = []

new File('resources/95003.m77t').eachLine { line ->
  //println line
  tokens = line.tokenize('\t')
  println tokens
  id = tokens[0]
  lat = tokens[4]
  lon = tokens[5]
  z = tokens[8]
  rows << [id, lat, lon, z]
}

def jsonFile = new File('resources/95003.json')
def ptMap = [:]
rows.each { row ->

    // Create the GeoJSON feature
    def jb = new JsonBuilder()
    Map feature = jb {
        type 'Feature'
        geometry {
            type 'Point'
            coordinates( [row] )
        }
        properties {
            id
        }
    }

    jb.toPrettyString()
    println jb

}
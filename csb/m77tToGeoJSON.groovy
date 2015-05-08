import groovy.json.JsonBuilder

def id
def lat
def lon
def z
def tokens = []
def pts = []
def ids = []

new File( 'resources/95003.m77t' ).eachLine { line ->
  //println line
  tokens = line.tokenize( '\t' )
  println tokens
  id = tokens[0]
  lat = tokens[4]
  lon = tokens[5]
  z = tokens[8]
  ids << id
  pts << [lat, lon, z]
}

def jsonFile = new File( 'resources/95003.json' )
def sb = new StringBuffer( )
def i = 0
pts.each { pt ->
    // Create the GeoJSON feature
    def jb = new JsonBuilder( )
    Map feature = jb {
        type 'Feature'
        geometry {
            type 'Point'
            coordinates( [pt] )
        }
        properties {
          "id:" ids[i]
        }
    }

    
    sb <<= "${jb.toPrettyString( )}\n"
    i++
}

println sb
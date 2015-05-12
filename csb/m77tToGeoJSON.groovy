import groovy.json.JsonBuilder

def id
def lat
def lon
def z
def tokens = []
def pts = []
def ids = []

def sb = new StringBuffer( )

def metaJb =  new JsonBuilder()
Map meta = metaJb {
    type "FeatureCollection"
    crs {
        type "name"
        properties {
            name "EPSG:4326"
        }
    }
    properties {
        convention "CSB 1.0"
        platform {
            type "Ship"
            name "NG Endeavor; NG Explorer"
            sensors { 
                sounder {
                    type "Single beam sounder"
                    make "Furuno echo sounder"
                }
                gps {
                    make ""
                    model ""
                }
            }
            
        }
        providerContactPoint {
            hasEmail "explore@expeditions.com"
        }
        processorContactPoint {
            hasEmail "explore@expeditions.com"
        }
        ownerContactPoint {
            hasEmail "explore@expeditions.com"
        }
        depthUnits "meters"
        timeUnits "UTC"
    }    
}
sb << "${metaJb.toPrettyString()}\n\"features\": ["

new File( 'resources/95003-10.m77t' ).eachLine { line ->
  //println line
  tokens = line.tokenize( '\t' )
  //println tokens
  id = tokens[0]
  lat = tokens[4]
  lon = tokens[5]
  z = tokens[8]
  ids << id
  pts << [lat, lon, z]
}

def jsonFile = new File( 'resources/95003.json' )
def i = 0
pts.each { pt ->
    // Create the GeoJSON feature
    def featJb = new JsonBuilder( )
    Map feature = featJb {
        type 'Feature'
        geometry {
            type 'Point'
            coordinates( [pt] )
        }
        properties {
          "id:" ids[i]
        }
    }

    
    sb <<= "${featJb.toPrettyString( )}\n"
    
    i++
}
sb << "]"

println sb
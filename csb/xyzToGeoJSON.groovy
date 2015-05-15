import groovy.json.JsonBuilder

def id
def lat
def lon
def z
def tokens = []
def pts = []
def ids = []

def sb = new StringBuffer( )
int i = 0
new File( 'resources/Arctic_September_2014_ver1.txt' ).eachLine { line ->
    //println line
    try {
        if (i > 1 && i < 10000) {
            tokens = line.tokenize( ' ' )
            //println tokens
            //id = tokens[0]
            lat = Double.parseDouble(tokens[0])
            lon = Double.parseDouble(tokens[1])
            z = Double.parseDouble(tokens[2])
            //ids << id
            pts << [lat, lon, z]
            println i            
        }
    } catch (Exception e) {
        e.printStackTrace()
    }
    i++
}
def jsonFile = new File( 'resources/Arctic_September_2014_ver1.json' )


def features = [ ]
pts.each { pt ->
    // Create the GeoJSON feature
    def featJb = new JsonBuilder(  )
    features << featJb {
        type 'Feature'
        geometry {
            type 'Point'
            coordinates( [pt[1],pt[0]] )
        }
        properties {
          depth pt[2]
        }
    }
    
}


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

meta.put( "features", features )
println "${metaJb.toPrettyString( )}"
jsonFile.write("${metaJb.toPrettyString( )}")


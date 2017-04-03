set -ex

IMG=$1
TILE_WIDTH=$2
TILE_HEIGHT=$3
NAME=$4
FLIF_OPTS=$5

convert ${IMG} -crop ${TILE_WIDTH}x${TILE_HEIGHT} \
          -set filename:tile "%[fx:page.y/${TILE_HEIGHT}+1]-%[fx:page.x/${TILE_WIDTH}+1]" \
          +repage +adjoin "${NAME}-%[filename:tile].png"

parallel flif ${FLIF_OPTS} -I {} {.}.flif ::: ${NAME}-*.png

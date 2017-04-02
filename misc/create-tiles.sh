set -ex

IMG=$1
TILEX=$2
TILEY=$3
NAME=$4

convert ${IMG} -crop ${TILEX}x${TILEY} \
          -set filename:tile "%[fx:page.y/${TILEY}+1]-%[fx:page.x/${TILEX}+1]" \
          +repage +adjoin "${NAME}-%[filename:tile].png"

parallel flif -I {} {.}.flif ::: ${NAME}-*.png

## t-flif

Support for tiled FLIF images. With the help of `t-flif`, a large image can be stored as a 2D matrix of tiles and a sub-image
can be decoded on the fly.

Why use tiles? The FLIF format (as of writing) interweaves pixel and bit data from all over the image. Hence it is not possible
to decode a sub-image of a FLIF file. Nor is it possible to parallelise the decoding. This is a bottle-neck for large images.

By splitting the image into smaller tiles, only the required subset of data can be decoded and it can be decoded in parallel, utilising
all the processing cores available in the CPU.

## Status
This is a very early, but useable, prototype. It is a peg in the ground to collect more feedback and
to evolve the idea further.

Currently, this tool just calls the FLIF CLI tools for decoding, but that will change later. In fact, a lot
of the implementation details will change later.

## How to use
* Install FLIF CLI tool such that it is available in `$PATH`
* Create the tiles. The `misc/create-tiles.sh` script is provided for convenience.
* Install sbt (Scala build tool)
* Type `sbt run --help` for the command line help. The various options define the size and location of tiles, the
  dimensions of the subimage and other decoding options.

## Features
* Decode an arbitrary sub-image of the tile-set
* Supports down sampling the image while decoding

## Roadmap

### Immediate
* Various simple optimisations, such as [caching the decoded output](https://github.com/hrj/t-flif/issues/1), [parallel decoding](https://github.com/hrj/t-flif/issues/2)
* [Support for different types of images](https://github.com/hrj/t-flif/issues/3) (grey-scale, RGB, RGBA, 16-bit, etc)
* [An `http` server](https://github.com/hrj/t-flif/issues/4) with [zoomable, pannable demo](https://github.com/hrj/t-flif/issues/5)
* [Support for interpolated output](https://github.com/hrj/t-flif/issues/6)
* Re-encode output as FLIF

### Long term
* Javascript version of t-flif using ScalaJS and PolyFLIF.
* More complex optimisations, such as embedding the decoder, using a low-level language, etc
* Support for more image formats
* Fast re-encoding of output in FLIF format

## Copyright and License

Copyright 2017 Harshad RJ

Licensed under Apache V2 (See LICENSE.txt)

# demo-rsocket

## CLI Examples

A simple and effective tool for calling the exposed routes is rsc (https://github.com/making/rsc)

I find that it's easiest to run from a docker container to avoid any OS related issues, as the source is arch-dependent.

### Docker

Build a container image via:

```shell
$ docker buildx build --platform linux/amd64 . -f - -t localhost/rsc <<EOF
  FROM ubuntu
  ADD https://github.com/making/rsc/releases/download/0.9.1/rsc-x86_64-pc-linux /bin/rsc
  RUN chmod +x /bin/rsc
EOF  
```

Then run the rsc command, pointed at your local host with:

```shell
$ docker run --add-host host.docker.internal:host-gateway localhost/rsc \
  /bin/rsc --stream -r demo.stream -d '{"count":5}' tcp://host.docker.internal:7000
```
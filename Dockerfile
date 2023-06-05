FROM sharifuli/polynote-jupyter-scala-spark-ds:latest

ENV PYSPARK_ALLOW_INSECURE_GATEWAY=1

#VOLUME /opt/polynote/notebooks/examples

EXPOSE 8192 7777 4040

CMD ["polynote"]
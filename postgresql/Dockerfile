FROM library/postgres

COPY init.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_USER=rave
ENV POSTGRES_DB=rave

ENTRYPOINT ["docker-entrypoint.sh"]

CMD ["postgres"]

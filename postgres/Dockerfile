FROM postgres:13
ENV LC_ALL en_DK.UTF-8
ENV LANG en_DK.UTF-8
ENV LANGUAGE en_DK.utf-8
RUN sed -i -e 's/# en_DK.UTF-8 UTF-8/en_DK.UTF-8 UTF-8/' /etc/locale.gen && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=en_DK.UTF-8
ENV TZ 'Europe/Oslo'
ENV PGTZ 'Europe/Oslo'
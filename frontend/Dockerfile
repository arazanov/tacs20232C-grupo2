FROM node:alpine
WORKDIR /code
COPY package.json /code
COPY yarn.lock /code
RUN yarn install
COPY . /code
CMD ["yarn", "run", "start"]
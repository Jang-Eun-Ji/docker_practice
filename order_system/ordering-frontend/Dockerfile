FROM node:20

WORKDIR /app


# 파일은 .으로 폴더는 /로 넘어간다잉
COPY . .

RUN npm install

ENTRYPOINT [ "npm", "run", "serve" ]
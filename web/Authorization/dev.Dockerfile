FROM node:16-alpine 

# Set the working directory to /app inside the container
WORKDIR /app

# Copy app files
COPY . .

COPY package.json package-lock.json ./

RUN npm install --silent

# ==== BUILD =====
# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
#RUN npm ci

# Install Vite globally
RUN npm install
#RUN npm install -g create-vite
#RUN npm install vite

# ==== RUN =======
# Set the env to "production"
ENV NODE_ENV production

# Expose the port on which the app will be running (5173 is the default that `vite` uses)
EXPOSE 5173

# Start the app
#RUN npm install --save-dev laravel-mix
COPY . ./
CMD ["npm", "run", "dev"]
# Use an official Node runtime as a parent image
FROM node:20.12.2

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the package.json and package-lock.json (if available)
COPY package*.json ./

# Install npm compatible with lockfileVersion@2
RUN npm install -g npm@8.10.0

# Copy the entire project folder into the container
COPY . .

# Install dependencies
RUN npm install

# Expose the port the app runs on
EXPOSE 3000

# Run the app when the container launches
CMD ["npm", "start"]

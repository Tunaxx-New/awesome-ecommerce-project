# Use the Bayes Impact React base image
FROM bayesimpact/react-base:latest

# Set the working directory inside the container
WORKDIR /usr/src/app

# Clone the GitHub repository into the working directory
# Replace 'your-repo-url.git' with the actual repository URL
RUN git clone https://github.com/Tunaxx-New/React-Diploma.git .

# Optionally, checkout a specific branch or commit
# RUN git checkout your-branch-or-commit

# Install dependencies
RUN npm install

# Expose the port the app runs on
EXPOSE 3000

# Use CMD to start the React app
CMD ["npm", "start"]

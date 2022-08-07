import axios from "axios";

export default axios.create({
  baseURL: "http://ec2-18-118-154-167.us-east-2.compute.amazonaws.com:8080",
  // withCredentials: true,
});

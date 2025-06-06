const userService = require('../services/userService');

exports.register = async (req, res) => {
  const result = await userService.register(req.body);
  res.json(result);
};

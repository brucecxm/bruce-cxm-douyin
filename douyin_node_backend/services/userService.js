const User = require('../models/user');

exports.register = async ({ username, password }) => {
  const user = await User.create({ username, password });
  return { message: 'User created', user };
};

// ESM 兼容版本的 global/window
var win;

if (typeof window !== 'undefined') {
    win = window;
} else if (typeof global !== 'undefined') {
    win = global;
} else if (typeof self !== 'undefined') {
    win = self;
} else {
    win = {};
}

export default win;

module.exports = (api) =>
{
    api.cache(false);

    return {
        "presets": [
            "@babel/preset-env",
        ],
        "plugins": [
            "@babel/proposal-class-properties",
            "@babel/proposal-object-rest-spread"
        ]
    };
};

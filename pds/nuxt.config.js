const pkg = require('./package')

const isProd = process.env.NODE_ENV === 'production'

module.exports = {
  mode: 'spa',

  /*
   ** Headers of the page
   */
  head: {
    title: "Garderie",
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: pkg.description }
    ],
    script: [
      { src: 'https://widget.cloudinary.com/global/all.js'
      }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
      { rel: 'stylesheet', href: 'https://fonts.googleapis.com/css2?family=Comfortaa:wght@300&display=swap' },
      { rel: 'stylesheet', href: 'https://use.fontawesome.com/releases/v5.8.1/css/all.css', integrity: 'sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf', crossorigin: "anonymous"}
    ]
  },

  /*
   ** Customize the progress-bar color
   */
  loading: { color: '#fff' },

  /*
   ** Global CSS
   */
  css: ['element-ui/lib/theme-chalk/index.css'
  ],

  /*
   ** Plugins to load before mounting the App
   */
  plugins: [
    '~/plugins/element-ui'
  ],

  /*
   ** Nuxt.js modules
   */
  modules: [
    '@nuxtjs/axios',
    '@nuxtjs/auth'
  ],

  axios: {
    proxy: false,
    retry: {retries: 3},
    browserBaseURL: process.env.NUXT_API_URL
  },
  auth: {
    strategies: {
      local: {
        endpoints: {
          login: { url: '/signin', method: 'post', propertyName: 'token' },
          logout: false,
          user: { url: '/users/me', propertyName: false },
          child: {url: '/children', methode: 'post', propertyName: false},
          family: {url: '/families', methode: 'post', propertyName: false}
        },
        tokenRequired: true,
        globalToken: true,
        tokenType: false,
        tokenName: 'X-Auth-Token'            }
    },
    redirect: {
      login: "/login",
      logout: "/login",
      home: '/'
    }
  },

  router: {
    middleware: ['auth']
  },

  build: {
    transpile: [/^element-ui/],
    extend(config, ctx) {
    }
  }
}

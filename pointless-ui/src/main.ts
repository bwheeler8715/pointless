import './assets/main.css'
import './assets/tailwind.css'

import {createApp} from 'vue'
import App from './App.vue'
import router from './router'

const response = await fetch('/config.json');
const runtimeConfig = await response.json();

const pointless = createApp(App)
pointless.provide('config', runtimeConfig)
pointless.use(router)
pointless.mount('#app')
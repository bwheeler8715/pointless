import './assets/main.css'
import './assets/tailwind.css'

import {createApp} from 'vue'
import App from './App.vue'
import router from './router'

const configResponse = await fetch(`/config.json?t=${new Date().getTime()}`);
const config = await configResponse.json();

const pointless = createApp(App)
pointless.provide('config', config)
pointless.use(router)
pointless.mount('#app')
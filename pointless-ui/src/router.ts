import {createRouter, createWebHistory} from 'vue-router'

import WelcomeView from "./views/WelcomeView.vue";
import OrganizerView from './views/OrganizerView.vue'
import VoterView from './views/VoterView.vue'
import RoomView from "@/views/RoomView.vue";
import CreateView from "./views/CreateView.vue";
import JoinView from "./views/JoinView.vue";
import ObserveView from "./views/ObserveView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/welcome',
        },
        {
            path: '/welcome',
            component: WelcomeView,
        },
        {
            path: '/create',
            component: CreateView,
        },
        {
            path: '/join',
            component: JoinView,
        },
        {
            path: '/join/:roomId',
            component: JoinView,
        },
        {
            path: '/observe',
            component: ObserveView,
        },
        {
            path: '/room/:roomId',
            component: RoomView,
            meta: {inRoom: true},
            children: [
                {
                    path: 'organizer/:organizerCode',
                    component: OrganizerView,
                    meta: {inRoom: true, organizer: true},
                },
                {
                    path: 'voter',
                    component: VoterView,
                    meta: {inRoom: true}
                }
            ]
        }
    ]
})

export default router
<script setup lang="ts">
import {computed, inject, onMounted, onUnmounted, provide, ref} from "vue";
import {RouterView, useRoute} from "vue-router";
import {type Room, useRoomService} from "@/composables/roomService.ts";

import Voters from "@/components/Voters.vue";
import Results from "@/components/Results.vue";

const config: any = inject('config')
const roomService = useRoomService()
const route = useRoute()
let room = ref<Room>({
    id: '',
    name: '',
    voters: [],
    votingOpen: false,
    currentTickerNumber: '',
    currentTicketSummary: '',
    voteCounts: {},
    averageVote: 0.0
});
let eventSource: EventSource | null;

const roleText = computed(() => {
    if (route.path.includes('organizer')) return 'the Organizer'
    if (route.path.includes('voter')) return 'a Voter'
    return 'an Observer'
})

onMounted(async () => {
    const roomId = route.params.roomId as string;
    room.value = await roomService.getRoom(roomId);

    eventSource = new EventSource(config.API_URL + `/room/${roomId}/stream`);

    // Listen for generic messages
    eventSource.addEventListener('room-update', (event) => {
        room.value = JSON.parse(event.data);
    });
});

onUnmounted(() => {
    if (eventSource != null) {
        eventSource.close()
    }
});

provide('room', room);
</script>

<template>
    <div class="grid grid-cols-1 gap-[2rem]">
        <div class="grid grid-cols-1 gap-[0.5rem] text-center">
            <h1 class="text-3xl font-bold">You are in room {{ room.name }}</h1>
            <h1 class="text-2xl font-bold">You are participating as {{ roleText }}</h1>
        </div>

        <div class="grid grid-cols-1 gap-[0.5rem] text-center min-h-16">
            <h1 v-if="room.currentTickerNumber" class="text-xl font-bold">Currently voting for
                {{ room.currentTickerNumber }}</h1>
            <h1 v-if="room.currentTickerNumber" class="text-m font-bold">{{ room.currentTicketSummary }}</h1>
        </div>

        <div class="grid grid-cols-2 gap-[3rem] items-start">
            <Voters :voters="room.voters" :voting-open="room.votingOpen"/>
            <div class="grid grid-cols-1 gap-[3rem]">
                <RouterView/>
                <Results v-if="!room.votingOpen && room.currentTickerNumber"
                         :vote-counts="room.voteCounts"
                         :average="room.averageVote"/>
            </div>
        </div>

    </div>
</template>

<style scoped></style>
<script setup lang="ts">
</script>

<template>
    <div class="grid grid-cols-1 gap-[3rem] text-center justify-items-center">
        <h1 class="text-5xl font-bold">Welcome to Pointless</h1>

        <div class="grid grid-cols-1 gap-[1rem] text-center justify-items-center">
            <h1 class="text-3xl font-bold">Are you here to:</h1>
            <RouterLink to="/create"
                        class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
                Create a Room?
            </RouterLink>
            <RouterLink to="/join"
                        class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
                Join a Room?
            </RouterLink>
            <RouterLink to="/observe"
                        class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
                Observe a Room?
            </RouterLink>
        </div>
    </div>
</template>

<style scoped></style>
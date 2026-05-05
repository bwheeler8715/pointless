<script setup lang="ts">
defineProps<{
    isOpen: boolean;
    title: string;
    message?: string;
}>();

const emit = defineEmits(['close']);
</script>

<template>
    <!-- Use Teleport to render the modal at the end of <body> for better layering -->
    <Teleport to="body">
        <div v-if="isOpen"
             @click.self="emit('close')"
             class="flex fixed inset-0 z-[100] items-center justify-center bg-black bg-opacity-50">
            <!-- Modal Content -->
            <div class="rounded-lg w-full max-w-md p-6 mx-4 shadow-xl bg-white">
                <h3 class="text-xl font-semibold mb-4 text-black">{{ title }}</h3>

                <div class="mb-6">
                    <p v-if="message" class="text-gray-600">{{ message }}</p>
                    <!-- Slot for flexible content -->
                    <slot></slot>
                </div>

                <button @click="emit('close')"
                        class="rounded-xl px-5 py-2 font-semibold text-white bg-sky-500 hover:bg-sky-700">
                    Close
                </button>
            </div>
        </div>
    </Teleport>
</template>

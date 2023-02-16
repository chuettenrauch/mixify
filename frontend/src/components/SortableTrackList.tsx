import {Box, Stack} from "@mui/material";
import {DragDropContext, Draggable, Droppable, DropResult} from "@hello-pangea/dnd";
import {DragIndicator as DragIndicatorIcon} from "@mui/icons-material";
import FlippableTrackCard from "./FlippableTrackCard";
import React from "react";
import Mixtape from "../types/mixtape";
import Track from "../types/track";

export default function SortableTrackList({mixtape, onUpdateTrack, onDeleteTrack, onMoveTrack}: {
    mixtape: Mixtape,
    onUpdateTrack: (updatedTrack: Track) => void,
    onDeleteTrack: (deleted: Track) => void
    onMoveTrack: (sourceIndex: number, destinationIndex: number) => void
}) {


    const onDragEnd = (result: DropResult) => {
        if (!result.destination) {
            return;
        }

        onMoveTrack(result.source.index, result.destination.index);
    }

    return (
        <DragDropContext onDragEnd={onDragEnd}>
            <Droppable droppableId="droppable">
                {provided => (
                    <Stack
                        spacing={2}
                        ref={provided.innerRef}
                        {...provided.droppableProps}
                    >
                        {mixtape.tracks.map((track, index) => (
                            <Draggable key={track.id} draggableId={track.id} index={index}>
                                {provided => (
                                    <Box
                                        ref={provided.innerRef}
                                        {...provided.draggableProps}
                                        {...provided.dragHandleProps}
                                        sx={{display: "flex", alignItems: "center"}}
                                    >
                                        <DragIndicatorIcon/>
                                        <FlippableTrackCard
                                            key={track.id}
                                            mixtape={mixtape}
                                            track={track}
                                            onEdit={onUpdateTrack}
                                            onDelete={onDeleteTrack}
                                        />
                                    </Box>
                                )}
                            </Draggable>
                        ))}
                        {provided.placeholder}
                    </Stack>
                )}
            </Droppable>
        </DragDropContext>
    );
}
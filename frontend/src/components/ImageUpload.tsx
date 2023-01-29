import {Container, IconButton, Skeleton, Stack} from "@mui/material";
import {PhotoCamera as PhotoCameraIcon} from "@mui/icons-material";
import React, {ChangeEvent, useMemo, useState} from "react";
import FileMetadata from "../types/file-metadata";
import {FileApi} from "../api/mixify-api";

export default function ImageUpload({imageUrl = null, onUpload}: {
    imageUrl: string|null,
    onUpload: (fileMetadata: FileMetadata) => void,
}) {
    const [imagePreview, setImagePreview] = useState<string | null>(imageUrl ?? null);

    const fileReader: FileReader = useMemo<FileReader>(() => {
        const reader = new FileReader();
        reader.addEventListener("load", () => {
            setImagePreview(reader.result as string);
        }, false);

        return reader;
    }, []);

    const onImageChange = async (e: ChangeEvent<HTMLInputElement>) => {
        const {files} = e.target;

        if (!files || files.length === 0) {
            setImagePreview(null);
            return;
        }

        const fileToUpload: File = files[0];

        showImagePreview(fileToUpload);
        uploadFile(fileToUpload);
    }

    const showImagePreview = (file: File) => {
        fileReader.readAsDataURL(file);
    }

    const uploadFile = async (file: File) => {
        const fileMetadata: FileMetadata = await FileApi.uploadFile(file);

        onUpload(fileMetadata);
    }

    return (
        <Stack
            spacing={2}
            width="100%"
            maxWidth={(theme) => theme.breakpoints.values.sm}
        >
            <Container sx={{height: 0, overflow: "hidden", paddingTop: "100%", position: "relative"}}>
                {imagePreview
                    ? <img src={imagePreview} alt="preview" style={{
                        position: "absolute",
                        top: 0,
                        left: 0,
                        objectFit: "cover",
                        width: "100%",
                        height: "100%"}}
                    />
                    : <Skeleton variant="rectangular" animation={false} width="100%" height="100%" sx={{
                        position: "absolute",
                        top: 0,
                        left: 0,
                    }}/>

                }
            </Container>

            <IconButton aria-label="upload image" component="label" sx={{zIndex: 1}}>
                <input hidden accept="image/*" type="file" onChange={onImageChange}/>
                <PhotoCameraIcon/>
            </IconButton>
        </Stack>
    );
}
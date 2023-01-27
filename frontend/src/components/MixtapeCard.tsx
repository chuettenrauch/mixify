import Mixtape from "../types/Mixtape";
import {Backdrop, Box, Container, IconButton, ListItemIcon, Menu, MenuItem, Paper, Typography} from "@mui/material";
import {Close as CloseIcon, Edit as EditIcon, MoreVert as MoreVertIcon} from "@mui/icons-material";
import React from "react";

export default function MixtapeCard({mixtape}: {
    mixtape: Mixtape
}) {
    const [mixtapeMenu, setMixtapeMenu] = React.useState<null | HTMLElement>(null);

    const mixtapeMenuId = `mixtape-${mixtape.id}-menu`;
    const mixtapeMenuOpen = Boolean(mixtapeMenu);

    return (
      <Paper square={true} elevation={1} sx={{display: "flex", p: 2, position: "relative"}}>
          <Box sx={{border: 1, width: 150}}>
              <img src={`/api/files/${mixtape.image}`} alt={mixtape.title}/>
          </Box>
          <Container sx={{display: "flex", flexDirection: "column", justifyContent: "center"}}>
              <Typography variant="h3">{mixtape.title}</Typography>
              <Typography>{mixtape.createdAt}</Typography>
          </Container>
          <IconButton onClick={(e) => setMixtapeMenu(e.currentTarget)}
                      aria-controls={mixtapeMenuOpen ? mixtapeMenuId : undefined}
                      aria-haspopup="true"
                      aria-expanded={mixtapeMenuOpen ? 'true' : undefined}
                      sx={{alignSelf: "flex-start", position: "absolute", top: 5, right: 0}}>
              <MoreVertIcon/>
          </IconButton>

          <Menu
              id={mixtapeMenuId}
              anchorEl={mixtapeMenu}
              open={mixtapeMenuOpen}
              onClose={() => setMixtapeMenu(null)}
              onClick={() => setMixtapeMenu(null)}
          >
              <MenuItem>
                  <ListItemIcon>
                      <EditIcon fontSize="small" />
                  </ListItemIcon>
                  Edit
              </MenuItem>
              <MenuItem>
                  <ListItemIcon>
                      <CloseIcon fontSize="small" />
                  </ListItemIcon>
                  Delete
              </MenuItem>
          </Menu>

          <Backdrop open={mixtapeMenuOpen} sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}/>
      </Paper>
    );
}
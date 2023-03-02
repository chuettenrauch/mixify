import {
    AppBar,
    Box,
    Container,
    Toolbar,
} from "@mui/material";
import {Link} from "react-router-dom";
import logo from "../logo.png";
import AccountMenu from "./account/AccountMenu";

export default function HeaderNav() {
    return (
        <AppBar position="sticky">
            <Container maxWidth="sm">
                <Toolbar disableGutters>
                    <Link to="/">
                        <Box sx={{filter: "invert(1)", display: "flex", alignItems: "center"}}>
                            <img src={logo} width="150" alt="mixify logo"/>
                        </Box>
                    </Link>

                    <Box sx={{ marginLeft: "auto" }}>
                        <AccountMenu/>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}
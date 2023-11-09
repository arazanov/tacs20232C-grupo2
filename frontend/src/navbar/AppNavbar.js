import {Nav, Navbar, NavbarBrand, NavItem, NavLink} from 'reactstrap';

export default function AppNavbar() {
    return <Navbar color="light" light expand="md">
        <NavbarBrand>
            <img alt="empanada" src={require("./empanada.png")} style={{ height: 40, width: 40 }}/>
            {' '}Empanadapp
        </NavbarBrand>
        <Nav>
            <NavItem>
                <NavLink href={"/profile"}>Perfil</NavLink>
            </NavItem>
            <NavItem>
                <NavLink href={"/"}>Cerrar sesión</NavLink>
            </NavItem>
        </Nav>

    </Navbar>;
}
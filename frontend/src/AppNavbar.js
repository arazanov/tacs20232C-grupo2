import React, {useState} from 'react';
import {Navbar, NavbarBrand} from 'reactstrap';

export default function AppNavbar() {
    const [isOpen, setState] = useState(false);

    function toggle() {
        setState(!isOpen);
    }

    return <Navbar color="dark" dark expand="md">
        <NavbarBrand href="/">Logout</NavbarBrand>
    </Navbar>;
}
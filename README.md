<div align="center">
    <img src="https://media.nocars.tk/lock.svg" alt="logo" width="50" />
    <h1>AllesLocker</h1>
</div>

AllesLocker is a web-based access management system. It lets you manage electronic locks, assign access rights to persons, and integrate with <a href="https://github.com/AllesLockr/Backend/wiki#Implemented-Vendors">implemented</a> third-party lock vendors — all from a single dashboard. The architecture enables easy integration of additional vendors.

<p align="center">
    <img src="https://media.nocars.tk/alles-locker.png" alt="logo" width="600"/>
</p>

## Architecture

```
┌──────────┐     ┌──────────┐     ┌─────────┐
│ Frontend │────▶│ Backend  │────▶│ MariaDB │
│  :5173   │     │  :8080   │     │  :3306  │
└──────────┘     └──────────┘     └─────────┘
                        │
                        ▼
                 ┌──────────┐
                 │  Vendor  │
                 │ APIs     │
                 └──────────┘
```

## Getting Started

For further information, please visit the [wiki](https://github.com/AllesLockr/Backend/wiki).

## Repositories

- [Frontend](https://github.com/AllesLockr/Frontend)
- [Backend](https://github.com/AllesLockr/Backend)

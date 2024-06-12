import './Tag.css';

function Tag({ types = [""], texts = [""] }) {
    const getBadgeColor = (type) => {
        if (type && type.toLowerCase)
            type = type.toLowerCase();

        switch (type) {
            case 'new':
                return 'var(--badge-color-new)';
            case 'popular':
            case 'success':
                return 'var(--badge-color-popular)';
            case 'sale':
            case 'warn':
                return 'var(--badge-color-sale)';
            case 'limited':
            case 'fail':
                return 'var(--badge-color-limited)';
            default:
                return 'var(--badge-color-default)';
        }
    }
    return (
        <div className='tags d-flex'>
            {texts.map((text, index) => (
                <span className="tag" style={{ backgroundColor: getBadgeColor(types[index]) }}>{text}</span>
            ))}
        </div>
    );
}

export default Tag;